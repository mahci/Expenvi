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
    private final boolean toLog = true;
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
     * Log Mouse motions
     * @param subBlockNum
     * @param trialNum
     * @param me
     * @return STATUS
     */
//    public STATUS logMotion(int subBlockNum, int trialNum,
//                           MouseEvent me) {
//        if (!enabled) return STATUS.LOG_DISABLED;
//
//        try {
//            if (motionLogFile == null) { // Open only if not opened before
//                motionLogFile = new PrintWriter(new FileWriter(motionLogPath, true));
//            }
//
//            // Create and write the log
//            String logStr = Experimenter.get().getTechOrdinal() + SEP +
//                            Experimenter.get().getPhaseOrdinal() + SEP +
//                            subBlockNum + SEP +
//                            trialNum + SEP +
//                            me;
//            motionLogFile.println(logStr);
//            motionLogFile.flush();
//
//            return STATUS.SUCCESS;
//
//        } catch (IOException | NullPointerException e) {
//            return STATUS.ERR_LOG_FILES;
//        }
//    }

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
     * Log the start of a phase
     * @param phase PHASE
     * @param technique Config.TECH
     * @return STATUS
     */
    public STATUS logPhaseStart(Experimenter.PHASE phase, Configs.TECH technique) {

        // Send log command to the Moose
        MooseServer.get().sendMssg(Strs.MSSG_TECHNIQUE, technique.toString());
        MooseServer.get().sendMssg(Strs.MSSG_BEG_PHS, phase.toString());

        return STATUS.SUCCESS;

        // Create a directory for this phase
//        String phaseDirPath = participDir + "/" +
//                phase + "-" +
//                technique + "--" +
//                Utils.nowDateTime();
//        if (createDir(phaseDirPath) == 0) {
////            phaseDir = phaseDirPath;
//
//
//
//            return STATUS.SUCCESS;
//        } else {
//            return STATUS.ERR_PHASE_DIR;
//        }
    }

    /**
     * Log the start of a block
     * @param blkNum Block number
     * @return STATU
     */
    public STATUS logBlockStart(int blkNum) {
        if (!enabled) return STATUS.CANCELLED;

        // Create 3 files (3 levels) for the block
        String blkStr = phaseDir + "/" + BLK_FILE_PFX + blkNum + "-";
        try {
            blkAllLogPath = blkStr + "ALL.txt";
            blkAllLog = new PrintWriter(new FileWriter(
                    blkAllLogPath, true));
            blkAllLog.println(Utils.nowTimeMilli());
            blkAllLog.println(blkSep);
            blkAllLog.flush();

            blkStrLogPath = blkStr + "STR.txt";
            blkStrLog = new PrintWriter(new FileWriter(
                    blkStrLogPath, true));
            blkStrLog.println(Utils.nowTimeMilli());
            blkStrLog.println(blkSep);
            blkStrLog.flush();

            blkTrgLogPath = blkStr + "TRG.txt";
            blkTrgLog = new PrintWriter(new FileWriter(
                    blkTrgLogPath, true));
            blkTrgLog.println(Utils.nowTimeMilli());
            blkTrgLog.println(blkSep);
            blkTrgLog.flush();

            // Sync the Moose
            MooseServer.get().sendMssg(Strs.MSSG_BEG_BLK, String.valueOf(blkNum));

            return STATUS.SUCCESS;

        } catch (IOException e) {
            e.printStackTrace();
            return STATUS.ERR_BLOCK_FILES;
        }

    }

    /**
     * Log the start of a trial
     * @param trNum Trial number
     * @param trial FittsTrial (for all the info)
     * @return STATUS
     */
    public STATUS logTrialStart(int trNum, FittsTrial trial) {
        if (!enabled) return STATUS.CANCELLED;

        try {
            blkStrLog = new PrintWriter(new FileWriter(
                    blkStrLogPath, true));
            blkStrLog.println("Trial #" + trNum);
            blkStrLog.println(trial);
            blkStrLog.flush();

            blkTrgLog = new PrintWriter(new FileWriter(
                    blkTrgLogPath, true));
            blkTrgLog.println("Trial #" + trNum);
            blkTrgLog.println(trial);
            blkTrgLog.flush();

            blkAllLog = new PrintWriter(new FileWriter(
                    blkAllLogPath, true));
            blkAllLog.println("Trial #" + trNum);
            blkTrgLog.println(trial);
            blkTrgLog.flush();

            return STATUS.SUCCESS;
        } catch (NullPointerException | IOException e) {
            return STATUS.ERR_BLOCK_FILES;
        }

    }

    /**
     * Log an event on the start
     * @param ve VouseEvent
     * @param d Distance to the center of the Start (mm)
     * @return STATUS
     */
    public STATUS logStartEvent(VouseEvent ve, double d) {
        if (!enabled) return STATUS.CANCELLED;

        try {
            if (blkStrLog == null) {
                blkStrLog = new PrintWriter(new FileWriter(
                        blkStrLogPath, true));
            }
            blkStrLog.println(ve);
            blkStrLog.println(String.format("Dist to center = %.2f", d));
            blkStrLog.flush();

            return STATUS.SUCCESS;

        } catch (NullPointerException | IOException e) {
            return STATUS.ERR_BLOCK_FILES;
        }
    }

    /**
     * Log an event on the target
     * @param ve VouseEvent
     * @param d Distance to the center of the Target (mm)
     * @return STATUS
     */
    public STATUS logTargetEvent(VouseEvent ve, double d) {
        if (!enabled) return STATUS.CANCELLED;

        try {
            blkTrgLog = new PrintWriter(new FileWriter(
                    blkTrgLogPath, true));
            blkTrgLog.println(ve);
            blkTrgLog.println(String.format("Dist to center = %.2f", d));
            blkTrgLog.flush();

            return STATUS.SUCCESS;

        } catch (NullPointerException | IOException e) {
            return STATUS.ERR_BLOCK_FILES;
        }
    }

    /**
     * Log a hit/miss on the Target
     * @param result "HIT" or "MISS"
     * @param addInfo Additional info (e.g. miss -> double click on Start!)
     * @return STATUS
     */
    public STATUS logTargetAttempt(String result, String addInfo) {
        if (!enabled) return STATUS.CANCELLED;

        try {
            blkTrgLog = new PrintWriter(new FileWriter(
                    blkTrgLogPath, true));
            blkTrgLog.print(result);
            if(!addInfo.equals("")) blkTrgLog.println(" - " + addInfo);
            else blkTrgLog.print("\n");

            blkTrgLog.flush();

            return STATUS.SUCCESS;

        } catch (NullPointerException | IOException e) {
            return STATUS.ERR_BLOCK_FILES;
        }
    }

    public STATUS logHomingTime(long hTime) {
        if (!enabled) return STATUS.CANCELLED;

        try {
            blkStrLog = new PrintWriter(new FileWriter(
                    blkStrLogPath, true));
            blkTrgLog.println("--- Homing time = " + hTime);
            blkTrgLog.flush();
            blkTrgLog.close();

            return STATUS.SUCCESS;

        } catch (NullPointerException | IOException e) {
            return STATUS.ERR_BLOCK_FILES;
        }
    }

    /**
     * Log everything!
     * @param me MouseEvent
     * @return STATUS
     */
    public STATUS logAll(MouseEvent me) {
        if (!enabled) return STATUS.CANCELLED;

        try {
            if (blkAllLog == null) {
                blkAllLog = new PrintWriter(new FileWriter(
                        blkAllLogPath, true));
            }
            blkAllLog.println(me);
            blkAllLog.flush();

            return STATUS.SUCCESS;

        } catch (NullPointerException | IOException e) {
            return STATUS.ERR_BLOCK_FILES;
        }
    }

    /**
     * Log everything!
     * @param ve VouseEvent
     * @return STATUS
     */
    public STATUS logAll(VouseEvent ve) {
        if (!enabled) return STATUS.CANCELLED;

        try {
            blkAllLog = new PrintWriter(new FileWriter(
                    blkAllLogPath, true));
            blkAllLog.println(ve);
            blkAllLog.flush();

            return STATUS.SUCCESS;

        } catch (NullPointerException | IOException e) {
            return STATUS.ERR_BLOCK_FILES;
        }
    }
    /**
     * Append a simple separator for separation
     * @return STATUS
     */
    public STATUS logTrialRepeat() {
        if (!enabled) return STATUS.CANCELLED;

        try {
            blkStrLog = new PrintWriter(new FileWriter(
                    blkStrLogPath, true));
            blkStrLog.println(trlSep);
            blkStrLog.flush();

            blkTrgLog = new PrintWriter(new FileWriter(
                    blkTrgLogPath, true));
            blkTrgLog.println(trlSep);
            blkTrgLog.flush();

            blkAllLog = new PrintWriter(new FileWriter(
                    blkAllLogPath, true));
            blkAllLog.println(trlSep);
            blkAllLog.flush();

            return STATUS.SUCCESS;

        } catch (NullPointerException | IOException e) {
            return STATUS.ERR_BLOCK_FILES;
        }
    }

    public STATUS logTrialEnd() {
        if (!enabled) return STATUS.CANCELLED;

        try {
            blkStrLog = new PrintWriter(new FileWriter(
                    blkStrLogPath, true));
            blkStrLog.println(trlSep);
            blkStrLog.flush();
            blkStrLog.close();

            blkTrgLog = new PrintWriter(new FileWriter(
                    blkTrgLogPath, true));
            blkTrgLog.println(trlSep);
            blkTrgLog.flush();
            blkTrgLog.close();

            blkAllLog = new PrintWriter(new FileWriter(
                    blkAllLogPath, true));
            blkAllLog.println(trlSep);
            blkAllLog.flush();
            blkAllLog.close();

            return STATUS.SUCCESS;

        } catch (NullPointerException | IOException e) {
            return STATUS.ERR_BLOCK_FILES;
        }
    }

    /**
     * Log the start of an experiment
     * @param participID Participant ID
     * @param dateTime Beginning data and time of the experiment
     */
//    public int logExpStart(int participID, LocalDateTime dateTime) {
//        // Enable and set participant ID
////        this.participID = participID;
//        this.enabled = true;
//
//        // Create a directory for the participant (if not already existing)
//        String ptcDirPath = logDirPath + "/" + PTC_FILE_PFX + participID;
//        createDir(ptcDirPath);
//
//        // Create a directory for the experiment
//        String expDirPath = ptcDirPath + "/" + Experimenter.get().getTechnique() + "--" + dateTime;
//        createDir(expDirPath);
//
//        // Create dirs for three levels
//        spcLogDirPath = expDirPath + "/SPEC/";
//        genLogDirPath = expDirPath + "/GEN/";
//        allLogDirPath = expDirPath + "/ALL/";
//
//        createDir(spcLogDirPath);
//        createDir(genLogDirPath);
//        createDir(allLogDirPath);
//
//        // Send start-of-experiment message to the Moose
//        MooseServer.get().sendMssg(
//                Strs.MSSG_BEG_EXP,
//                Experimenter.get().getTechnique() + "--" + dateTime);
//
//        return 0;
//    }
//
//    /**
//     * Start logging a block
//     * @param blockNum Block number
//     * @param time Beginning time of the block
//     * @return STATUS
//     */
//    public int logBlockStart(int blockNum, LocalTime time) {
//
//        // Create block files for all the levels
//        if (!enabled) return 1;
//
//        try {
//            spcBlockLogFile = new PrintWriter(new FileWriter(
//                    spcLogDirPath + BLK_FILE_PFX + blockNum + ".txt", true));
//            spcBlockLogFile.println("Start: " + time);
//            spcBlockLogFile.println(blkSep);
//
//            genBlockLogFile = new PrintWriter(new FileWriter(
//                    genLogDirPath + BLK_FILE_PFX + blockNum + ".txt", true));
//            genBlockLogFile.println("Start: " + time);
//            genBlockLogFile.println(blkSep);
//
//            allBlockLogFile = new PrintWriter(new FileWriter(
//                    allLogDirPath + BLK_FILE_PFX + blockNum + ".txt", true));
//            allBlockLogFile.println("Start: " + time);
//            allBlockLogFile.println(blkSep);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Sync logging with the Moose
//        MooseServer.get().sendMssg(
//                Strs.MSSG_BEG_BLK,
//                String.valueOf(blockNum));
//
//        return 0;
//
//    }

    /**
     * Trial ended: add the trial separator
     * @return STATUS
     */
//    public int logTrialEnd() {
//        if (!enabled) return 1;
//
//        if (spcBlockLogFile != null) spcBlockLogFile.println(trlSep);
//        if (genBlockLogFile != null) genBlockLogFile.println(trlSep);
//        if (allBlockLogFile != null) allBlockLogFile.println(trlSep);
//
//        // Sync logging with the Moose
//        MooseServer.get().sendMssg(Strs.MSSG_END_TRL);
//
//        return 0;
//    }

    /**
     * Close the block
     * @param time Block's end time
     * @return STATUS
     */
//    public int logBlockEnd(LocalTime time) {
//        if (!enabled) return 1;
//
//        if (spcBlockLogFile != null) {
//            spcBlockLogFile.println(blkSep);
//            spcBlockLogFile.println("End: " + time);
//            spcBlockLogFile.close();
//        }
//        if (genBlockLogFile != null) {
//            genBlockLogFile.println(blkSep);
//            genBlockLogFile.println("End: " + time);
//            genBlockLogFile.close();
//        }
//        if (allBlockLogFile != null) {
//            allBlockLogFile.println(blkSep);
//            allBlockLogFile.println("End: " + time);
//            allBlockLogFile.close();
//        }
//
//        // Sync logging with the Moose
//        MooseServer.get().sendMssg(Strs.MSSG_END_BLK);
//
//        return 0;
//    }
//
//    /**
//     * Log a MouseEvent
//     * @param e MouseEvent
//     * @return STATUS
//     */
//    public int log(MouseEvent e, LOG_LEVEL lvl, LocalTime time) {
//        if (!enabled) return 1;
//
//        switch (lvl) {
//        case SPEC:
//            if (spcBlockLogFile != null) spcBlockLogFile.println(e.paramString() + "--" + time);
//            else System.out.println(TAG + "No spec block log file found!");
//            break;
//        case GEN:
//            if (genBlockLogFile != null) genBlockLogFile.println(e.paramString() + "--" + time);
//            else System.out.println(TAG + "No gen block log file found!");
//            break;
//        case ALL:
//            if (allBlockLogFile != null) allBlockLogFile.println(e.paramString() + "--" + time);
//            else System.out.println(TAG + "No all trial log file found!");
//            break;
//        }
//        return 0;
//    }

    /**
     * Log a VouseEvent on a level
     * @param ve VouseEvent
     * @return STATUS
     */
//    public int log(VouseEvent ve, LOG_LEVEL lvl) {
////        if (!enabled) return 0;
////
////        switch (lvl) {
////        case SPEC:
////            if (spcBlockLogFile != null) spcBlockLogFile.println(ve);
////            else System.out.println(TAG + "No spec block log file found!");
////            break;
////        case GEN:
////            if (genBlockLogFile != null) genBlockLogFile.println(ve);
////            else System.out.println(TAG + "No gen block log file found!");
////            break;
////        case ALL:
////            if (allBlockLogFile != null) allBlockLogFile.println(ve);
////            else System.out.println(TAG + "No all trial log file found!");
////            break;
////        }
////        return 0;
////    }
////
////    /**
////     * Log on all levels
////     * @param ve VouseEvnet
////     * @return STATUS
////     */
////    public int log(VouseEvent ve) {
////        if (!enabled) return 1;
////
////        if (spcBlockLogFile != null) spcBlockLogFile.println(ve);
////        else System.out.println(TAG + "No spec block log file found!");
////
////        if (genBlockLogFile != null) genBlockLogFile.println(ve);
////        else System.out.println(TAG + "No gen block log file found!");
////
////        if (allBlockLogFile != null) allBlockLogFile.println(ve);
////        else System.out.println(TAG + "No all trial log file found!");
////
////        return 0;
////    }
////
////    /**
////     * Log a timing
////     * @param time Long time :)
////     * @param title What time is it?
////     * @param lvl Log level
////     * @return STATUS
////     */
////    public int log(long time, String title, LOG_LEVEL lvl) {
////        if (!enabled) return 1;
////
////        switch (lvl) {
////        case SPEC -> {
////            if (spcBlockLogFile != null) spcBlockLogFile.println(title + ": " + time);
////            else System.out.println(TAG + "No spec block log file found!");
////        }
////        case GEN -> {
////            if (genBlockLogFile != null) genBlockLogFile.println(title + ": " + time);
////            else System.out.println(TAG + "No gen block log file found!");
////        }
////        case ALL -> {
////            if (allBlockLogFile != null) allBlockLogFile.println(title + ": " + time);
////            else System.out.println(TAG + "No all trial log file found!");
////        }
////        }
////
////        return 0;
////    }

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
