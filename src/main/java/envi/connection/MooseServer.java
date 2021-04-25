package envi.connection;

import envi.tools.Config;
import envi.experiment.Experimenter;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class MooseServer {

    private String TAG = "[[MooseServer]] ";
    private boolean toLog = true;
    //============================================
    private static MooseServer self; // for singleton

    private ServerSocket serverSocket;
    private BufferedReader inBR;
    private PrintWriter outPW;
    private boolean isConnected = false;

    public PublishSubject<String> actionSubject; // For publishing the actions

    private @NonNull Observable<String> listenerObserver;

    /**
     * Get instance
     * @return
     */
    public static MooseServer get() {
        if (self == null) self = new MooseServer();
        return self;
    }

    /**
     * Constructor
     */
    private MooseServer() {
        actionSubject = PublishSubject.create();

        listenerObserver = Observable.fromAction(() -> {
            // Continously read lines from the Moose until get disconnected
            String line;
            do {
                line = inBR.readLine();
                // publish the action
                actionSubject.onNext(line);
            } while(isConnected);
        });
    }

    /**
     * Start the server
     */
    public void start() {
        try {
            // Open socket
            if (toLog) System.out.println(TAG + "Starting server...");
            serverSocket = new ServerSocket(Config._netPort);
            if (toLog) System.out.println(TAG + "Socket opened, waiting for the Moose...");
            Socket socket = serverSocket.accept();
            if (toLog) System.out.println(TAG + "Connection accepted!");

            // Create streams
            inBR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outPW = new PrintWriter(socket.getOutputStream());

            // Get connection request from the Moose
            String line = inBR.readLine();
            if(toLog) System.out.println(TAG + "First Moose message: " + line);

            if (Objects.equals(line, Config.MSSG_MOOSE)) { // Correct message
                // Confirm
                sendMssg(Config.MSSG_CONFIRM);
                if (toLog) {
                    System.out.println(TAG + "Moose connected! Receiving actions...");
                    System.out.println("------------------------------------------");
                }

                isConnected = true;

                // Send the participants ID
                sendMssg(Config.MSSG_PID + "_" + Experimenter.get().getPID());

                // Pass the PublishSubject to the Bot for listening
//                MooseBot.get().startBot(actionSubject);

                // Start listening to incoming messages from the Moose
                listenerObservable().subscribe();

                // If interactino is not the Mouse, send actions to the Moose
                Experimenter.get().getExpSubject().subscribe(state -> {
                    if (Config._interaction != Config.INTERACTION.MOUSE_LCLICK) sendMssg(state);
                });

            }

        } catch (IOException ioException) {
            System.out.println("Problem in starting the server!" + ioException);
            ioException.printStackTrace();
        }
    }

    /**
     * Close the server
     */
    public void close() {
        sendMssg(Config.NET_DISCONNECT);
    }

    /**
     * Create bbservable for input commands from Moose
     * @return An Observable that publishes the received action
     */
    private @NonNull Observable<Object> listenerObservable() {
        return Observable.fromAction(() -> {
            // Continously read lines from the Moose until get disconnected
            String line;
            do {
                if (toLog) System.out.println(TAG + "Getting Moose commands...");
                line = inBR.readLine();
                if (line != null) {
                    // publish the action
                    actionSubject.onNext(line);

                    if (toLog) System.out.println(TAG + line + " recieved");
                }
            } while(inBR != null && isConnected);
        }).subscribeOn(Schedulers.io());
    }

    /**
     * Send messages to the Moose
     * @param mssg Message
     */
    private void sendMssg(String mssg) {
        if (outPW != null) {
            outPW.println(mssg);
            outPW.flush();
            if (toLog) System.out.println(TAG + mssg + " sent");
        } else {
            System.out.println(TAG + "Output PrintWriter not available!");
        }
    }

}
