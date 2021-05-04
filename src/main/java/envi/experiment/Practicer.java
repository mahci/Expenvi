package envi.experiment;

import envi.gui.MainFrame;
import envi.gui.PracticePanel;
import envi.tools.Config;


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

        // Set the config
        Config.setFromFile();

        // Start the practice (show-down)
        MainFrame.get().showPanel(new PracticePanel());
    }

}
