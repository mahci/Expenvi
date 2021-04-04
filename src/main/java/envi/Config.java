package envi;

import java.awt.*;

public class Config {

    // Colors
    public static Color COLOR_TEXT_NRM      = Color.BLUE;
    public static Color COLOR_TEXT_ERR      = Color.RED;
    public static Color COLOR_TEXT_START    = Color.DARK_GRAY;
    public static Color COLOR_STACLE_DEF    = Color.decode("#3dcf38");
    public static Color COLOR_STACLE_CLK    = Color.decode("#3d6e3b");
    public static Color COLOR_TARCLE_DEF    = Color.decode("#e05c2f");

    // Positions
    public static int STACLE_X = 600; // Start circle X
    public static int STACLE_Y = 400; // Start circle Y

    // Sizes
    public static int STACLE_RAD = 20;
    public static int LR_MARGIN = 100;   // Left/right margin
    public static int TB_MARGIN = 100;   // Top bottom margin

    // Text
    public static int TEXT_X = 200; // From the right edge
    public static int TEXT_Y = 50; // From the top
    public static int TEXT_PAN_W = 100;
    public static int TEXT_PAN_H = 100;
    public static int ERROR_Y = 50; // X is calculated dynamically (from middle of the screen)
    public static String FONT_STYLE = "Sans-serif";
    public static int FONT_SIZE = 14;

    // Experiment
    public static int N_BLOCKS_IN_EXPERIMENT = 2;

}
