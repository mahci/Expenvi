package envi.experiment;

import envi.gui.MainFrame;
import envi.gui.ShowCasePanel;
import envi.tools.Config;

public class ShowCaser {

    private String TAG = "[[ShowCaser]] ";
    private boolean toLog = false;

    private static ShowCaser self = null; // for singleton

    // ===============================================================================

    /**
     * Get the instance
     * @return the singleton instance
     */
    public static ShowCaser get() {
        if (self == null) self = new ShowCaser();
        return self;
    }

    /**
     * Constructor
     */
    private ShowCaser() {

    }

    // -------------------------------------------------------------------------------

    /***
     * Start the practice
     */
    public void startShowCase() {
        if (toLog) System.out.println(TAG + "Practice started");

        // Set the config
        Config.setFromFile();

        // Start the practice (show-down)
        MainFrame.get().showPanel(new ShowCasePanel());
    }

}
