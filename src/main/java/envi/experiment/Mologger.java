package envi.experiment;

import envi.action.VouseEvent;
import envi.tools.Config;
import envi.tools.Utils;

import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
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

    private String blkSep = "==============================================================";
    private String trlSep = "--------------------------------------------------------------";

    // Experiment
    private int participID;
    private boolean enabled = false;

    // Naming
    private static String SPEC_LOGS_DIR;
    private static String GEN_LOGS_DIR;
    private static String ALL_LOGS_DIR;

    private static String logDirPath;
    private final String topLogDirPath;

    // Paths to keep the current dirc
    private String specLogDirPath;
    private String genLogDirPath;
    private String allLogDirPath;

    private PrintWriter blockLogFile;

    private PrintWriter specBlockLogFile;
    private PrintWriter genBlockLogFile;
    private PrintWriter allBlockLogFile;

    // Log level
    public static enum LOG_LEVEL {
        SPEC,
        GEN,
        ALL
    }

    // ===============================================================================

    /**
     * Constructor
     */
    private Mologger() {
        // Create the top logging directory
        Path parentPath = Paths.get("").toAbsolutePath().getParent();
        topLogDirPath = parentPath.toAbsolutePath().toString() + "/Expenvi-Logs/";
        createDir(topLogDirPath);

    }


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
     * Log the start of an experiment
     * @param participID Participant ID
     * @param dateTime Beginning data and time of the experiment
     */
    public int logExpStart(int participID, LocalDateTime dateTime) {
        // Enable and set participant ID
        this.participID = participID;
        this.enabled = true;

        // Create a directory for the participant (if not already existing)
        String ptcDirPath = topLogDirPath + "/" + PTC_FILE_PFX + participID;
        createDir(ptcDirPath);

        // Create a directory for the experiment
        String expDirPath = ptcDirPath + "/" + Config._interaction + "--" + dateTime;
        createDir(expDirPath);

        // Create dirs for three levels
        specLogDirPath = expDirPath + "/SPEC/";
        genLogDirPath = expDirPath + "/GEN/";
        allLogDirPath = expDirPath + "/ALL/";

        createDir(specLogDirPath);
        createDir(genLogDirPath);
        createDir(allLogDirPath);

        return 0;
    }

    /**
     * Start logging a block
     * @param blockNum Block number
     * @param time Beginning time of the block
     * @return Status
     */
    public int logBlockStart(int blockNum, LocalTime time) {

        // Create block files for all the levels
        if (!enabled) return 1;

        try {
            specBlockLogFile = new PrintWriter(new FileWriter(
                    specLogDirPath + BLK_FILE_PFX + blockNum + ".txt", true));
            specBlockLogFile.println("Start: " + time);
            specBlockLogFile.println(blkSep);

            genBlockLogFile = new PrintWriter(new FileWriter(
                    genLogDirPath + BLK_FILE_PFX + blockNum + ".txt", true));
            genBlockLogFile.println("Start: " + time);
            genBlockLogFile.println(blkSep);

            allBlockLogFile = new PrintWriter(new FileWriter(
                    allLogDirPath + BLK_FILE_PFX + blockNum + ".txt", true));
            allBlockLogFile.println("Start: " + time);
            allBlockLogFile.println(blkSep);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;

    }

    /**
     * Trial ended: add the trial separator
     * @return Status
     */
    public int logTrialEnd() {
        if (!enabled) return 1;

        if (specBlockLogFile != null) specBlockLogFile.println(trlSep);
        if (genBlockLogFile != null) genBlockLogFile.println(trlSep);
        if (allBlockLogFile != null) allBlockLogFile.println(trlSep);
        return 0;
    }

    /**
     * Close the block
     * @param time Block's end time
     * @return Status
     */
    public int logBlockEnd(LocalTime time) {
        if (!enabled) return 1;

        if (specBlockLogFile != null) {
            specBlockLogFile.println(blkSep);
            specBlockLogFile.println("End: " + time);
            specBlockLogFile.close();
        }
        if (genBlockLogFile != null) {
            genBlockLogFile.println(blkSep);
            genBlockLogFile.println("End: " + time);
            genBlockLogFile.close();
        }
        if (allBlockLogFile != null) {
            allBlockLogFile.println(blkSep);
            allBlockLogFile.println("End: " + time);
            allBlockLogFile.close();
        }
        return 0;
    }

    /**
     * Log a MouseEvent
     * @param e MouseEvent
     * @return Status
     */
    public int log(MouseEvent e, LOG_LEVEL lvl, LocalTime time) {
        if (!enabled) return 1;

        switch (lvl) {
        case SPEC:
            if (specBlockLogFile != null) specBlockLogFile.println(e.paramString() + "--" + time);
            else System.out.println(TAG + "No spec block log file found!");
            break;
        case GEN:
            if (genBlockLogFile != null) genBlockLogFile.println(e.paramString() + "--" + time);
            else System.out.println(TAG + "No gen block log file found!");
            break;
        case ALL:
            if (allBlockLogFile != null) allBlockLogFile.println(e.paramString() + "--" + time);
            else System.out.println(TAG + "No all trial log file found!");
            break;
        }
        return 0;
    }

    /**
     * Log a VouseEvent on a level
     * @param ve VouseEvent
     * @return Status
     */
    public int log(VouseEvent ve, LOG_LEVEL lvl) {
        if (!enabled) return 0;

        switch (lvl) {
        case SPEC:
            if (specBlockLogFile != null) specBlockLogFile.println(ve);
            else System.out.println(TAG + "No spec block log file found!");
            break;
        case GEN:
            if (genBlockLogFile != null) genBlockLogFile.println(ve);
            else System.out.println(TAG + "No gen block log file found!");
            break;
        case ALL:
            if (allBlockLogFile != null) allBlockLogFile.println(ve);
            else System.out.println(TAG + "No all trial log file found!");
            break;
        }
        return 0;
    }

    /**
     * Log on all levels
     * @param ve VouseEvnet
     * @return Status
     */
    public int log(VouseEvent ve) {
        if (!enabled) return 1;

        if (specBlockLogFile != null) specBlockLogFile.println(ve);
        else System.out.println(TAG + "No spec block log file found!");

        if (genBlockLogFile != null) genBlockLogFile.println(ve);
        else System.out.println(TAG + "No gen block log file found!");

        if (allBlockLogFile != null) allBlockLogFile.println(ve);
        else System.out.println(TAG + "No all trial log file found!");

        return 0;
    }

    /**
     * Log a timing
     * @param time Long time :)
     * @param title What time is it?
     * @param lvl Log level
     * @return Status
     */
    public int log(long time, String title, LOG_LEVEL lvl) {
        if (!enabled) return 1;

        switch (lvl) {
        case SPEC -> {
            if (specBlockLogFile != null) specBlockLogFile.println(title + ": " + time);
            else System.out.println(TAG + "No spec block log file found!");
        }
        case GEN -> {
            if (genBlockLogFile != null) genBlockLogFile.println(title + ": " + time);
            else System.out.println(TAG + "No gen block log file found!");
        }
        case ALL -> {
            if (allBlockLogFile != null) allBlockLogFile.println(title + ": " + time);
            else System.out.println(TAG + "No all trial log file found!");
        }
        }

        return 0;
    }

    /**
     * Create a directory
     * @param path Directory path
     */
    private void createDir(String path) {
        Path dir = Paths.get(path);
        try {
            // Create the directory only if not existed
            if (!Files.isDirectory(dir)) Files.createDirectory(dir);
        } catch (IOException e) {
            if (toLog) System.out.println(TAG + "Problem in creating dir: " + path);
            e.printStackTrace();
        }
    }

}
