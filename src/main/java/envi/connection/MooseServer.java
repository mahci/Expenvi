package envi.connection;

import envi.tools.Configs;
import envi.experiment.Experimenter;
import envi.tools.Strs;
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
import java.net.SocketException;
import java.util.Objects;

public class MooseServer {

    private String TAG = "[[MooseServer]] ";
    private boolean toLog = true;
    // -------------------------------------------------------------------------------

    private static MooseServer self; // for singleton

    private ServerSocket serverSocket;
    private BufferedReader inBR;
    private PrintWriter outPW;
    private boolean isConnected = false;

    public PublishSubject<String> actionSubject; // For publishing the actions

//    private @NonNull Observable<String> listenerObserver;

    // ===============================================================================

    /**
     * Get instance
     * @return Singleton instance
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

//        listenerObserver = Observable.fromAction(() -> {
//            // Continously read lines from the Moose until get disconnected
//            String line;
//            do {
//                line = inBR.readLine();
//                // publish the action
//                actionSubject.onNext(line);
//            } while(isConnected);
//        });
    }

    /**
     * Start the server
     */
    public void start() {
        try {
            // Open socket
            if (toLog) System.out.println(TAG + "Starting server...");

            serverSocket = new ServerSocket(Configs._netPort);

            while (true) { // Keep the socket opened (while the programm is running)
                if (toLog) System.out.println(TAG + "Socket opened, waiting for the Moose...");

                Socket socket = serverSocket.accept();
                if (toLog) System.out.println(TAG + "Connection accepted!");

                // Create streams
                inBR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outPW = new PrintWriter(socket.getOutputStream());

                // Get connection request from the Moose
                String line = inBR.readLine();
                if (toLog) System.out.println(TAG + "First Moose message: " + line);

                if (Objects.equals(line, Strs.MSSG_MOOSE)) { // Correct message

                    sendInit(); // Send init messages to the Moose

                    if (toLog) {
                        System.out.println(TAG + "Moose connected! Receiving actions...");
                        System.out.println("------------------------------------------");
                    }

                    isConnected = true;

                    listenerObservable().subscribe(); // Start listening to incoming messages from the Moose
                }
            }

        } catch (IOException e) {
            System.out.println("Problem in starting the server: " + e);
//            e.printStackTrace();
        }
    }

    /**
     * Disconnect and close the socket (called when closing the application)
     */
    public void close() {
//        sendMssg(Configs.NET_DISCONNECT + "_" + "-");
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create observable for input commands from Moose
     * @return An Observable that publishes the received action
     */
    private @NonNull Observable<Object> listenerObservable() {
        return Observable.fromAction(() -> {
            // Continously read lines from the Moose until get disconnected
            String line;
            while(inBR != null && isConnected) {
                try {
                    if (toLog) System.out.println(TAG + "Getting Moose commands...");
                    line = inBR.readLine();
                    if (toLog) System.out.println(TAG + "line: " + line);

                    if (line != null) {

                        if (Objects.equals(line, Strs.MSSG_MOOSE)) { // Connection was reset, got MOOSE
                            sendInit();
                        } else { // Continuing commands
                            actionSubject.onNext(line);
                        }
                    }
                } catch (SocketException se) {
                    start();
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * Standard way to send a message with params
     * @param type Type of the message
     * @param param String
     */
    public void sendMssg(String type, String param) {
        if (!Objects.equals(param, "")) sendMssg(type + "-" + param);
        else sendMssg(type);
    }

    /**
     * Standard way to send a message with params
     * @param type Type of the message
     * @param param int
     */
    public void sendMssg(String type, int param) {
        if (!Objects.equals(param, "")) sendMssg(type + "-" + param);
        else sendMssg(type);
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
//            if (toLog) System.out.println(TAG + "Output PrintWriter not available!");
        }
    }

    /**
     * Send the init messages to the Moose to get it up to speed
     */
    private void sendInit() {
        sendMssg(Strs.MSSG_CONFIRM); // Confirm
        syncParticipant(Experimenter.get().getPID());
        syncTechnique(Experimenter.get().getTechnique());
    }

    /**
     * Update the Moose on the new technique
     */
    public void syncTechnique(Configs.TECH tech) {
        sendMssg(Strs.MSSG_TECHNIQUE, tech.toString());
    }

    /**
     * Sync the participant id with the Moose
     * @param pid int Participant ID
     */
    public void syncParticipant(int pid) {
        System.out.println(TAG + "Sync PID");
        sendMssg(Strs.MSSG_PID + "-" + pid);
    }

    /**
     * Sync the phase ordinal with the Moose
     * @param phase PHASE
     */
    public void syncPhase(Experimenter.PHASE phase) {
        System.out.println(TAG + "Sync phase");
        sendMssg(Strs.MSSG_BEG_PHS + "-" + phase.ordinal());
    }

}
