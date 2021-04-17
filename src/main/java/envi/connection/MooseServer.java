package envi.connection;

import envi.tools.Utils;
import envi.experiment.Experimenter;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Action;
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
        listenerObserver = Observable.fromAction(new Action() {

            @Override
            public void run() throws Throwable {
                // Continously read lines from the Moose until get disconnected
                String line;
                do {
                    line = inBR.readLine();
                    // publish the action
                    actionSubject.onNext(line);
                } while(isConnected);
            }
        });
    }

    /**
     * Start the server
     */
    public void start() {

        try {

            // Open socket
            System.out.println("Starting server...");
            serverSocket = new ServerSocket(Utils.CONN_PORT);
            Socket socket = serverSocket.accept();
            System.out.println("Server started!");

            // Create streams
            inBR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outPW = new PrintWriter(socket.getOutputStream());

            // Get connection request from the Moose
            String line = inBR.readLine();
//            System.out.println("First Moose Message: " + line);

            if (Objects.equals(line, Utils.MSSG_MOOSE)) { // Correct message
                // Confirm
                sendMssg(Utils.MSSG_CONFIRM);
                System.out.println("Moose connected! Receiving actions...");
                System.out.println("------------------------------------------");

                isConnected = true;

                // Send the participants ID
                sendMssg(Utils.MSSG_PID + "_" + Experimenter.get().getPID());

                // Pass the PublishSubject to the Bot for listening
//                MooseBot.get().startBot(actionSubject);

                // Start listening to incoming messages from the Moose
                listenerObservable().subscribe();

                // Start listening to Experimenter
                Experimenter.get().getExpSubject().subscribe(state -> {
                    sendMssg(state);
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
        sendMssg(Utils.NET_DISCONNECT);
    }

    /**
     * Create bbservable for input commands from Moose
     * @return An Observable that publishes the received action
     */
    private @NonNull Observable<Object> listenerObservable() {
        return Observable.fromAction(new Action() {

            @Override
            public void run() throws Throwable {
                // Continously read lines from the Moose until get disconnected
                String line;
                do {
                    System.out.println("Reading Moose commands...");
                    line = inBR.readLine();
                    System.out.println(TAG + "Recieved: " + line);
                    // publish the action
                    actionSubject.onNext(line);
                } while(inBR!=null && isConnected);
            }
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
            System.out.println(TAG + mssg + " sent");
        } else {
            System.out.println(TAG + "Out PrintWriter not open!");
        }
    }

}
