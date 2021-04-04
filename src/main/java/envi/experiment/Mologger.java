package envi.experiment;

import envi.action.VouseEvent;

import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Mologger {

    // Tag (for debugging)
    private final String TAG = "[[Mologger]] ";

    // Naming
    private static String LOGS_DIR = "/Users/mahmoud/Documents/Academics/PhD/Moose/Dev/Java/Logs/";
    private static String LOG_FILE_PFX = "Log-";
    private static String PTC_FILE_PFX = "PTC";
    private static String EXP_FILE_PFX = "EXP";
    private static String BLK_FILE_PFX = "BLK";

    private static Mologger self; // Singleton
    private List<MouseEvent>[] logDB;

    private PrintWriter logFile;
    private PrintWriter metaLogFile;
    private PrintWriter blockLogFile;

    /***
     * Constructor
     */
    private Mologger() {

    }


    /***
     * Singleton get instance
     * @return self
     */
    public static Mologger get() {
        if (self == null) self = new Mologger();
        return self;
    }

    /**
     * Create a directory for the participant
     * @param participID Participant ID
     */
    public void logParticipStart(int participID) {
        // Create a directory for the participant
        String ptcDirPath = LOGS_DIR + PTC_FILE_PFX + participID;
//        System.out.println(TAG + "Dir: " + ptcDirPath);
        Path dir = Paths.get(ptcDirPath);
        try {
            if (!Files.isDirectory(dir)) Files.createDirectory(dir); // Create the directory only if not existed
        } catch (IOException e) {
            System.out.println(TAG + "Problem with creating the directory!");
            e.printStackTrace();
        }
    }

    /**
     * Log the start of an experiment
     * @param participID Participant ID
     * @param expNum Experiment ID
     * @param dateTime Beginning data and time of the experiment
     */
    public void logExpStart(int participID, int expNum, LocalDateTime dateTime) {
        // Create the info file for the experiment
        String expFilePath = LOGS_DIR + PTC_FILE_PFX + participID + "/" +
                PTC_FILE_PFX + participID + " - " +
                EXP_FILE_PFX + expNum +
                ".txt";
        try {
            PrintWriter expLogFile = new PrintWriter(new FileWriter(expFilePath));
            expLogFile.println(dateTime); // Write the time on the first line
            expLogFile.println("--------------------------------------------------");
            expLogFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start logging a block
     * @param participID Participant ID
     * @param expNum Experiment number
     * @param blockNum Block number
     * @param time Beginning time of the block
     */
    public void logBlockStart(int participID, int expNum, int blockNum, LocalTime time) {
        // Experiment file path (for indicating the block starting time)
        String expFilePath = LOGS_DIR + PTC_FILE_PFX + participID + "/" +
                PTC_FILE_PFX + participID + " - " +
                EXP_FILE_PFX + expNum +
                ".txt";
        // Block file path
        String blkFilePath = LOGS_DIR + PTC_FILE_PFX + participID + "/" +
                PTC_FILE_PFX + participID +  " - " +
                EXP_FILE_PFX + expNum +  " - " +
                BLK_FILE_PFX + blockNum +
                ".txt";
        try {
            // Write the block start to the experiment file
            PrintWriter expPW = new PrintWriter(new FileWriter(expFilePath, true));
            expPW.println("Block " + blockNum + " -- start: " + time);
            expPW.close();

            // Create the block file
            blockLogFile = new PrintWriter(new FileWriter(blkFilePath));
            // Block file remains open...
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the block
     * @param participID Participant ID
     * @param expNum Experiment number
     * @param blockNum Block number
     * @param time Block's end time
     */
    public void logBlockEnd(int participID, int expNum, int blockNum, LocalTime time) {
        // Experiment file path (for indicating the block end time)
        String expFilePath = LOGS_DIR + PTC_FILE_PFX + participID + "/" +
                PTC_FILE_PFX + participID + " - " +
                EXP_FILE_PFX + expNum +
                ".txt";
        try {
            // Write the block start to the experiment file
            PrintWriter expPW = new PrintWriter(new FileWriter(expFilePath, true));
            expPW.println("Block " + blockNum + " -- end: " + time);
            expPW.close();

            // Close the block file
            if (blockLogFile != null) blockLogFile.close();
            else System.out.println("LBE: Block log file not created!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Log a MouseEvent
     * @param e MouseEvent
     */
    public void log(MouseEvent e, LocalTime eTime) {
        // Write the info to the file
        if (blockLogFile != null) blockLogFile.println(e.paramString() + " -- " + eTime);
        else System.out.println("LOG: Problem writing to block log file!");
    }

    /**
     * Log a VouseEvent
     * @param ve VouseEvent
     */
    public void log(VouseEvent ve) {
        // Write the info to the file
        if (blockLogFile != null) blockLogFile.println(ve);
        else System.out.println(TAG + "Problem writing to block log file!");
    }


    /**
     * Just adding a sep. between trials
     */
    public void logTrialEnd() {
        if (blockLogFile != null)
            blockLogFile.println("----------------------------------------------------------------------");
        else
            System.out.println("LTE: Block log file not created!");
    }

}
