package envi.experiment;

import envi.gui.MainFrame;
import envi.gui.PracticePanel;
import envi.tools.Config;
import envi.tools.Utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Practicer {

    private String TAG = "[[Practicer]] ";
    private boolean toLog = false;
    //=========================================================
    private static Practicer self = null; // for singleton

    // General params
    private int winW, winH;
    public int time = 0; // min

    // Panel to show
    private PracticePanel practicePanel;

    // Constructors ================================================================

    /**
     * Get the instance
     * @return the singleton instance
     */
    public static Practicer get() {
        if (self == null) self = new Practicer();
        return self;
    }

    /**
     * Constructor
     */
    private Practicer() {

    }

    // Practice ====================================================================

    /***
     * Start the practice
     */
    public void startPractice() {
        if (toLog) System.out.println(TAG + "Practice started");

        // Get the window size
        winW = MainFrame.get().getBounds().width;
        winH = MainFrame.get().getBounds().height;

        // Set the pixel values
        Utils.setPxValues();

        // Start the first trial
        practicePanel = new PracticePanel();
//        nextTrial();
    }


    /**
     * Create and show the next trial
     */
//    public void nextTrial() {
//        practicePanel = new PracticePanel(new FittsTrial(winW, winH), time);
//        MainFrame.get().showPanel(practicePanel);
//    }


}
