package envi.tools;

import org.checkerframework.checker.units.qual.A;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Config {

    // Colors
    public static Color COLOR_TEXT_NRM      = Color.BLUE;
    public static Color COLOR_TEXT_ERR      = Color.RED;
    public static Color COLOR_TEXT_START    = Color.DARK_GRAY;
    public static Color COLOR_STACLE_DEF    = Color.decode("#3dcf38");
    public static Color COLOR_STACLE_CLK    = Color.decode("#3d6e3b");
    public static Color COLOR_TARCLE_DEF    = Color.decode("#e05c2f");

    // Display
    public static final int BENQ_DPI = 90;
    public static final int DPI = BENQ_DPI;

    public static int NUM_SCREENS = 1; // Set programmatically
    public static int SCR_ID = 1; // Used in Main
    public static Rectangle SCR_BOUNDS; // Set programmatically (px)

    public static int WIN_W_MARGIN = 100;   // Left/right margin
    public static int WIN_H_MARGIN = 50;   // Top bottom margin

    public static int _winW, _winH;
    public static int _dispW, _dispH;

    // Text -----------------------------------------------
    public static int TEXT_X = 100; // From the right edge
    public static int TEXT_Y = 50; // From the top
    public static int ERROR_Y = 50; // X is calculated dynamically (from middle of the screen)
    public static String FONT_STYLE = "Sans-serif";
    public static int EXP_INFO_FONT_SIZE = 14;
    public static Font EXP_INFO_FONT = new Font(FONT_STYLE, Font.PLAIN, EXP_INFO_FONT_SIZE);
    public static Font S_FONT = new Font(FONT_STYLE, Font.PLAIN, EXP_INFO_FONT_SIZE);
    public static String MINUTES = " Minutes";

    // Messages --------------------------------------------
    public static final String MSSG_MOOSE       = "MOOSE";
    public static final String MSSG_CONFIRM     = "CONFIRM";
    public static final String MSSG_PID         = "PID";
    public static final String MSSG_BEG_EXP     = "BEGEXP";
    public static final String MSSG_BEG_BLK     = "BEGBLK";
    public static final String MSSG_END_BLK     = "ENDBLK";
    public static final String MSSG_END_TRL     = "ENDTRL";
    public static final String MSSG_BEGIN_LOG   = "BEGLOG";
    public static final String MSSG_END_LOG     = "ENDLOG";
    // -----------------------------------------------------

    // Network ---------------------------------------------
    public static final int CONN_PORT = 5000;
    public static final String NET_DISCONNECT   = "DISCONNECT";
    // -----------------------------------------------------

    // Experiment ==================================================================
    public static int _stacleRadMM = 8; // Stacle radius (mm)
    public static int _stacleRad; // Stacle radius (px) Set programmatically
    // Target radii (mm)
    public static List<Integer> _targetRadiiMM = new ArrayList<Integer>() {{
       add(5);
       add(10);
    }};
    // Distances (mm)
    public static List<Integer> _distancesMM = new ArrayList<Integer>() {{
        add(40);
        add(50);
    }};

    public static int _nBlocksInExperiment = 2; // Number of blocks in an experiment
    // The gesture for clicks
    public static enum GESTURE {
        SWIPE_LCLICK,
        TAP_LCLICK
    }
    public static GESTURE _action = GESTURE.SWIPE_LCLICK;
    public static boolean _vibrate = false; // Vibrate?

    // --- Practice
    public static int _practiceTime = 10; // Practice time (min)
    public static int _minTarRadMM = 5; // Minimum traget radius (mm)
    public static int DISP_H_RATIO_TAR_RAD = 2; // Maximum target radius = dispH / this
    public static enum PROCESS_STATE {
        SHOW_CASE,
        WARM_UP,
        EXPERIMENT
    }

    // --- ERRORS and TEXTs
    public static final String ERR_NOT_INSIDE   =
            "PLEASE CLICK INSIDE THE START CIRCLE";
    public static final String DIMSSG_BLOCK_FINISH =
            "Block finished! You can now take a break. Press OK when ready.";
    // =============================================================================




}
