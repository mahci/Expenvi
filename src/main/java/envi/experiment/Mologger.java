package envi.experiment;

import envi.action.Actions;
import envi.action.VouseEvent;
import envi.connection.MooseServer;
import envi.tools.Configs;
import envi.tools.STATUS;
import envi.tools.Strs;
import envi.tools.Utils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Mologger {

    private final String TAG = "[[Mologger]] ";
    private final boolean toLog = false;
    // -------------------------------------------------------------------------------

    private static Mologger self; // Singleton

    private static String PTC_FILE_PFX = "PTC";
    private static String EXP_FILE_PFX = "EXP";
    private static String BLK_FILE_PFX = "BLK";

    private static String PI = "P";
    private static String SEP = ";";

    private String blkSep = "==============================================================";
    private String trlSep = "--------------------------------------------------------------";

    // Experiment
    private boolean enabled = false;

    private String participDir;
    private String phaseDir;

    private String blkStrLogPath;
    private String blkTrgLogPath;
    private String blkAllLogPath;

    private PrintWriter blkStrLog;
    private PrintWriter blkTrgLog;
    private PrintWriter blkAllLog;

    // Naming
    private static String SPEC_LOGS_DIR;
    private static String GEN_LOGS_DIR;
    private static String ALL_LOGS_DIR;

    private static String logDirPath;

    // Paths to keep the current dirc
    private String spcLogDirPath;
    private String genLogDirPath;
    private String allLogDirPath;

    private PrintWriter blockLogFile;

    private PrintWriter spcBlockLogFile;
    private PrintWriter genBlockLogFile;
    private PrintWriter allBlockLogFile;

    // Log level
    public enum LOG_LEVEL {
        SPEC,
        GEN,
        ALL
    }

    // META and ALL log files
    private String metaLogPath;
    private PrintWriter metaLogFile;

    private String timeLogPath;
    private PrintWriter timeLogFile;

    private String eventLogPath;
    private PrintWriter eventLogFile;

    private String motionLogPath;
    private PrintWriter motionLogFile;



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
            // Create the META and ALL files for the participant
            metaLogPath = logDirPath +
                    PI + pID + "_" +
                    Utils.nowDateTime() + "_" +
                    "META.txt";

            timeLogPath = logDirPath +
                    PI + pID + "_" +
                    Utils.nowDateTime() + "_" +
                    "TIME.txt";

            eventLogPath = logDirPath +
                    PI + pID + "_" +
                    Utils.nowDateTime() + "_" +
                    "EVENT.txt";

//            motionLogPath = logDirPath +
//                    PI + pID + "_" +
//                    Utils.nowDateTime() + "_" +
//                    "MOTION.txt";

            // Open META file and write the column headers
            metaLogFile = new PrintWriter(new FileWriter(metaLogPath));
            metaLogFile.println(metaLogHeader());
            metaLogFile.flush();

            // Open TIME file and write the column headers
            timeLogFile = new PrintWriter(new FileWriter(timeLogPath));
            timeLogFile.println(timeLogHeader());
            timeLogFile.flush();

            // Open EVENT file and write the column headers
            eventLogFile = new PrintWriter(new FileWriter(eventLogPath));
            eventLogFile.println(eventLogHeader());
            eventLogFile.flush();

            // Open MOTION file and write the column headers
//            motionLogFile = new PrintWriter(new FileWriter(motionLogPath));
//            motionLogFile.println(motionLogHeader());
//            motionLogFile.flush();


            return STATUS.SUCCESS;

        } catch (IOException e) {
            e.printStackTrace();
            return STATUS.ERR_LOG_FILES;
        }
//        try {
//            blkAllLogPath = blkStr + "ALL.txt";
//            blkAllLog = new PrintWriter(new FileWriter(
//                    blkAllLogPath, true));
//            blkAllLog.println(Utils.nowTimeMilli());
//            blkAllLog.println(blkSep);
//            blkAllLog.flush();
//
//            blkStrLogPath = blkStr + "STR.txt";
//            blkStrLog = new PrintWriter(new FileWriter(
//                    blkStrLogPath, true));
//            blkStrLog.println(Utils.nowTimeMilli());
//            blkStrLog.println(blkSep);
//            blkStrLog.flush();
//
//            blkTrgLogPath = blkStr + "TRG.txt";
//            blkTrgLog = new PrintWriter(new FileWriter(
//                    blkTrgLogPath, true));
//            blkTrgLog.println(Utils.nowTimeMilli());
//            blkTrgLog.println(blkSep);
//            blkTrgLog.flush();
//
//            // Sync the Moose
//            MooseServer.get().sendMssg(Strs.MSSG_BEG_BLK, String.valueOf(blkNum));
//
//            return STATUS.SUCCESS;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return STATUS.ERR_BLOCK_FILES;
//        }

        // Create a directory for the participant (if not already existing)
//        String ptcDirPath = logDirPath + "/" + PTC_FILE_PFX + pID;
//        if (createDir(ptcDirPath) == 0) {
//            participDir = ptcDirPath;
//
//            // Send log command to the Moose
//            MooseServer.get().sendMssg(Strs.MSSG_PID, String.valueOf(pID));
//
//            return STATUS.SUCCESS;
//        } else {
//            return STATUS.ERR_PARTICIP_DIR;
//        }


    }

    /**
     * Log a row in the META file
     * @param technique
     * @param phase
     * @param subblock_num
     * @param trial_num
     * @param target_width
     * @param target_dist
     * @param target_dir
     * @param start_pos_x
     * @param start_pos_y
     * @param target_press_x
     * @param target_press_y
     * @param target_press_dist
     * @param target_press_time
     * @param target_release_x
     * @param target_release_y
     * @param target_release_dist
     * @param target_release_time
     * @param target_hit
     * @return
     */
    public STATUS logMeta(int technique, int phase, int subblock_num, int trial_num,
                          double target_width, double target_dist, int target_dir, int start_pos_x, int start_pos_y,
                          int target_press_x, int target_press_y, double target_press_dist, long target_press_time,
                          int target_release_x, int target_release_y, double target_release_dist, long target_release_time,
                          int target_hit) {
        System.out.println(TAG + "Log META");
        if (!enabled) return STATUS.LOG_DISABLED;

        try {
            if (metaLogFile == null) { // Open only if not opened before
                metaLogFile = new PrintWriter(new FileWriter(metaLogPath, true));
            }

            // Create and write the log
            String logStr = technique + SEP +
                    phase + SEP +
                    subblock_num + SEP +
                    trial_num + SEP +
                    Utils.double3Dec(target_width) + SEP +
                    Utils.double3Dec(target_dist) + SEP +
                    target_dir + SEP +
                    start_pos_x + SEP +
                    start_pos_y + SEP +
                    target_press_x + SEP +
                    target_press_y + SEP +
                    Utils.double3Dec(target_press_dist) + SEP +
                    target_press_time + SEP +
                    target_release_x + SEP +
                    target_release_y + SEP +
                    Utils.double3Dec(target_release_dist) + SEP +
                    target_release_time + SEP +
                    target_hit + SEP;

            metaLogFile.println(logStr);
            metaLogFile.flush();
//            metaLogFile.close();

            return STATUS.SUCCESS;

        } catch (IOException | NullPointerException e) {
            return STATUS.ERR_LOG_FILES;
        }

    }

    /**
     * Log different times
     * @param technique TECH
     * @param phase PHASE
     * @param sbNum Subblock number
     * @param sbDT subblock duration
     * @param homingTime Homing time
     * @return STATUS
     */
    public STATUS logTime(int technique, int phase, int sbNum, long sbDT, long homingTime) {
        System.out.println(TAG + "Log TIME");
        if (!enabled) return STATUS.LOG_DISABLED;

        try {
            if (timeLogFile == null) { // Open only if not opened before
                timeLogFile = new PrintWriter(new FileWriter(timeLogPath, true));
            }

            // Create and write the log
            String logStr = technique + SEP +
                    phase + SEP +
                    sbNum + SEP +
                    sbDT + SEP +
                    homingTime;

            timeLogFile.println(logStr);
            timeLogFile.flush();
//            timeLogFile.close();

            return STATUS.SUCCESS;

        } catch (IOException | NullPointerException e) {
            return STATUS.ERR_LOG_FILES;
        }

    }

    /**
     * Log an event
     * @param subBlockNum Subblock number
     * @param trialNum Trial number
     * @param me MouseEvent (either real or dummy)
     * @return STATUS
     */
    public STATUS logEvent(int technique, int phase, int subBlockNum, int trialNum, MouseEvent me) {
        if (!enabled) return STATUS.LOG_DISABLED;

        try {
            if (eventLogFile == null) { // Open only if not opened before
                eventLogFile = new PrintWriter(new FileWriter(eventLogPath, true));
            }

            // Create and write the log
            String logStr =
                    technique + SEP +
                    phase + SEP +
                    subBlockNum + SEP +
                    trialNum + SEP +
                    mouseEventToString(me);

            eventLogFile.println(logStr);
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
    public STATUS finishLogs() {
        try {
            if (metaLogFile != null) metaLogFile.close();
            if (motionLogFile != null) motionLogFile.close();
            if (eventLogFile != null) eventLogFile.close();
            if (motionLogFile != null) motionLogFile.close();

            return STATUS.SUCCESS;

        } catch (NullPointerException e) {
            return STATUS.ERR_LOG_FILES;
        }
    }


    /**
     * Get the header for the META file
     * @return String log header
     */
    private String metaLogHeader() {
        return "technique" + SEP +
                "phase" + SEP +
                "subblock_num" + SEP +
                "trial_num" + SEP +
                "target_width" + SEP +
                "target_dist" + SEP +
                "target_dir" + SEP +
                "start_position_x" + SEP +
                "start_psition_y" + SEP +

                "target_press_x" + SEP +
                "target_press_y" + SEP +
                "target_press_dist" + SEP +
                "target_press_time" + SEP +
                "target_release_x" + SEP +
                "target_release_y" + SEP +
                "target_release_dist" + SEP +
                "target_release_time" + SEP +
                "target_hit" + SEP;
    }

    /**
     * Get the header for the TIME file
     * @return String log header
     */
    private String timeLogHeader() {
        return "technique" + SEP +
                "phase" + SEP +
                "subblock_num" + SEP +
                "subblock_dur" + SEP +
                "homing_time" + SEP;
    }

    /**
     * Get the header for the EVENT file
     * @return String log header
     */
    private String eventLogHeader() {
        return "technique" + SEP +
                "phase" + SEP +
                "subblock_num" + SEP +
                "trial_num" + SEP +
                "action" + SEP +
                "cursor_pos_X" + SEP +
                "cursor_pos_Y" + SEP +
                "timestamp";
    }

    /**
     * Get the header for the MOTION file
     * @return String log header
     */
    private String motionLogHeader() {
        return "technique" + SEP +
                "phase" + SEP +
                "subblock_num" + SEP +
                "trial_num" + SEP +
                "mouse_event" + SEP +
                "timestamp";
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
