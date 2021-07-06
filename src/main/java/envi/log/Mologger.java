package envi.log;

import envi.tools.Prefs;
import envi.tools.STATUS;
import envi.tools.Utils;

import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Mologger {

    private final String TAG = "[[Mologger]] ";
    private final boolean toLog = false;
    // -------------------------------------------------------------------------------
    private static Mologger self; // Singleton

    private static String PI = "P";
    private static String SEP = ";";

    private boolean enabled = false; // To log or not to log...

    private static String logDirPath; // Main path for logs

    // Different log files
    private String trialLogPath;
    private PrintWriter trialLogFile;

    private String instantLogPath;
    private PrintWriter instantLogFile;

    private String timeLogPath;
    private PrintWriter timeLogFile;

    private String eventLogPath;
    private PrintWriter eventLogFile;

    // ===============================================================================

    /**
     * Singleton get instance
     * @return self
     */
    public static Mologger get() {
        if (self == null) {
            self = new Mologger();
        }
        return self;
    }

    /**
     * Constructor
     */
    private Mologger() {
        // Create the top logging directory
        Path parentPath = Paths.get("").toAbsolutePath().getParent();
        logDirPath = parentPath.toAbsolutePath() + "/Expenvi-Logs/";
        createDir(logDirPath);
    }


    /**
     * Enable/disbale the logging
     * @param onOff Boolean
     */
    public void setEnabled(boolean onOff) {
        enabled = onOff;
    }

    /**
     * Log when a new particiapnt starts (create folder)
     * @param pID Participant's ID
     * @return STATUS
     */
    public STATUS logParticipant(int pID) {

        try {
            // Create log files for the participant
            trialLogPath = logDirPath +
                    PI + pID + "_" +
                    Utils.nowDateTime() + "_" +
                    "TRIAL.txt";

            instantLogPath = logDirPath +
                    PI + pID + "_" +
                    Utils.nowDateTime() + "_" +
                    "INSTANT.txt";

            timeLogPath = logDirPath +
                    PI + pID + "_" +
                    Utils.nowDateTime() + "_" +
                    "TIME.txt";

            eventLogPath = logDirPath +
                    PI + pID + "_" +
                    Utils.nowDateTime() + "_" +
                    "EVENT.txt";

            // Open TRIAL file and write the column headers
            trialLogFile = new PrintWriter(new FileWriter(trialLogPath));
            trialLogFile.println(
                    GeneralLogInfo.getLogHeader() + Prefs.SEP +
                    TrialLogInfo.getLogHeader());
            trialLogFile.flush();

            // Open INSTANT file and write the column headers
            instantLogFile = new PrintWriter(new FileWriter(instantLogPath));
            instantLogFile.println(
                    GeneralLogInfo.getLogHeader() + Prefs.SEP +
                    InstantsLogInfo.getLogHeader());
            instantLogFile.flush();

            // Open TIME file and write the column headers
            timeLogFile = new PrintWriter(new FileWriter(timeLogPath));
            timeLogFile.println(
                    GeneralLogInfo.getLogHeader() + Prefs.SEP +
                    TimesLogInfo.getLogHeader());
            timeLogFile.flush();


            return STATUS.SUCCESS;

        } catch (IOException e) {
            e.printStackTrace();
            return STATUS.ERR_LOG_FILES;
        }

    }

    /**
     * Log info of the experiment
     * @param generalLogInfo GeneralLogInfo - general info
     * @param trialLogInfo TrialLogInfo - experiment info
     * @return STATUS
     */
    public STATUS logTrial(GeneralLogInfo generalLogInfo, TrialLogInfo trialLogInfo) {
        System.out.println(TAG + "Log META");
        if (!enabled) return STATUS.LOG_DISABLED;

        try {
            if (trialLogFile == null) { // Open only if not opened before
                trialLogFile = new PrintWriter(new FileWriter(trialLogPath, true));
            }

            trialLogFile.println(generalLogInfo.toLogString() + Prefs.SEP + trialLogInfo.toLogString());
            trialLogFile.flush();
//            metaLogFile.close();

            return STATUS.SUCCESS;

        } catch (IOException | NullPointerException e) {
            return STATUS.ERR_LOG_FILES;
        }

    }

    /**
     * Log info of intants
     * @param generalLogInfo GeneralLogInfo - general info
     * @param instantsLogInfo InstantsLogInfo - instant info
     * @return STATUS
     */
    public STATUS logInst(GeneralLogInfo generalLogInfo, InstantsLogInfo instantsLogInfo) {
        System.out.println(TAG + "Log INST");
        if (!enabled) return STATUS.LOG_DISABLED;

        try {
            if (instantLogFile == null) { // Open only if not opened before
                instantLogFile = new PrintWriter(new FileWriter(instantLogPath, true));
            }

            instantLogFile.println(generalLogInfo.toLogString() + Prefs.SEP + instantsLogInfo.toLogString());
            instantLogFile.flush();

            return STATUS.SUCCESS;

        } catch (NullPointerException | IOException e) {
            return STATUS.ERR_LOG_FILES;
        }
    }

    /**
     * Log different times
     * @param generalLogInfo GeneralLogInfo - general info
     * @param timesLogInfo TimesLogInfo - info about time
     * @return STATUS
     */
    public STATUS logTime(GeneralLogInfo generalLogInfo, TimesLogInfo timesLogInfo) {
        System.out.println(TAG + "Log TIME");
        if (!enabled) return STATUS.LOG_DISABLED;

        try {
            timeLogFile = new PrintWriter(new FileWriter(timeLogPath, true));
            timeLogFile.println(
                    generalLogInfo.toLogString() +
                    Prefs.SEP +
                    timesLogInfo.toLogString());
            timeLogFile.flush();
            timeLogFile.close();

            return STATUS.SUCCESS;

        } catch (IOException | NullPointerException e) {
            return STATUS.ERR_LOG_FILES;
        }

    }

    /**
     * Log an event
     * @param generalLogInfo GeneralLogInfo - general info
     * @return STATUS
     */
    public STATUS logEvent(GeneralLogInfo generalLogInfo, MouseEvent me) {
        if (!enabled) return STATUS.LOG_DISABLED;

        try {
            if (eventLogFile == null) { // Open only if not opened before
                eventLogFile = new PrintWriter(new FileWriter(eventLogPath, true));
            }

            eventLogFile.println(
                    generalLogInfo.toLogString() +
                    Prefs.SEP +
                    mouseEventToString(me));
            eventLogFile.flush();
//            eventLogFile.close();

            return STATUS.SUCCESS;

        } catch (IOException | NullPointerException e) {
            return STATUS.ERR_LOG_FILES;
        }
    }

    /**
     * Close all logs
     * @return STATUS
     */
    public STATUS closeLogs() {
        try {
            if (trialLogFile != null) trialLogFile.close();
            if (instantLogFile != null) instantLogFile.close();
            if (timeLogFile != null) timeLogFile.close();
            if (eventLogFile != null) eventLogFile.close();

            return STATUS.SUCCESS;

        } catch (NullPointerException e) {
            return STATUS.ERR_LOG_FILES;
        }
    }


    /**
     * Create a directory
     * @param path Directory path
     */
    private int createDir(String path) {
        Path dir = Paths.get(path);
        try {
            // Create the directory only if not existed
            if (!Files.isDirectory(dir)) Files.createDirectory(dir);
            return 0;
        } catch (IOException e) {
            if (toLog) System.out.println(TAG + "Problem in creating dir: " + path);
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * Log-appropriate toString() for the MouseEvent
     * @param me MouseEvent
     * @return String
     */
    public String mouseEventToString(MouseEvent me) {
        return me.getID() + SEP +
                me.getX() + SEP +
                me.getY() + SEP +
                me.getWhen();
    }


}
