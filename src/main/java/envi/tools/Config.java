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

    public static int WIN_HOR_MARGIN = 50;   // Left/right margin
    public static int WIN_VER_MARGIN = 50;   // Top bottom margin
    public static int NUM_SCREENS = 1; // Set programmatically
    public static int SCR_ID = 1; // Used in Main
    public static Rectangle SCR_BOUNDS; // Set programmatically (px)

    // Text
    public static int TEXT_X = 200; // From the right edge
    public static int TEXT_Y = 50; // From the top
    public static int TEXT_PAN_W = 100;
    public static int TEXT_PAN_H = 100;
    public static int ERROR_Y = 50; // X is calculated dynamically (from middle of the screen)
    public static String FONT_STYLE = "Sans-serif";
    public static int FONT_SIZE = 14;

    // Experiment ====================================================
    public static int _stacleRadMM = 20; // Default
    public static int _stacleRad; // (px) Set programmatically
    public static List<Integer> _targetRadiiMM = new ArrayList<Integer>() {{ // Defaults
       add(5);
       add(15);
       add(20);
       add(25);
    }};
    public static List<Integer> _distancesMM = new ArrayList<Integer>() {{ // Defaults
        add(40);
        add(50);
        add(60);
        add(70);
    }};
    public static int _nBlocksInExperiment = 2;
    public static enum GESTURE {
        SWIPE_LCLICK,
        TAP_LCLICK
    }
    public static GESTURE _action = GESTURE.SWIPE_LCLICK;
    public static boolean _vibrate = false;


}
