package envi.tools;

import java.awt.*;

public class Prefs {

    // COLORS
    public static Color COLOR_TEXT          = Color.BLACK;
    public static Color COLOR_ERROR         = Color.RED;
    public static Color COLOR_TITLES        = Color.DARK_GRAY;

    public static Color COLOR_START_DEF     = Color.decode("#3DCF38");
    public static Color COLOR_START_SEL     = Color.decode("#3D6E3B");
    public static Color COLOR_TARGET_DEF    = Color.decode("#1E90FF");
    public static Color COLOR_TARGET_SEL    = Color.decode("#1E20FF");

    public static Color COLOR_BREAK_BACK    = Color.decode("#7B1FA2");

    // DIMENSIONS & MARGINS
    public static int WIN_MARG_H_mm         = 30; // Window top & down margin (mm)
    public static int WIN_MARG_W_mm         = 70; // Window left & right margin (mm)

    public static int STAT_RECT_WIDTH_mm    = 30; // Width of the stats rectangle (mm)
    public static int STAT_RECT_HEIGHT_mm   = 8; // Height of the stats rectangle (mm)

    public static int STAT_MARG_X_mm        = 15; // Margin from the right edge of the screen (mm)
    public static int STAT_MARG_Y_mm        = 8; // Margin from the top edge of the screen (mm)

    public static int STAT_TEXT_X_PAD_mm    = 3; // Stats X padding (mm)
    public static int STAT_TEXT_Y_PAD_mm    = 6; // Stats Y padding (mm)

    public static int S_TEXT_X_OFFSET_mm    = 1; // S X offset from circle center (mm)
    public static int S_TEXT_Y_OFFSET_mm    = 2; // S Y offset from circle center (mm)

    // FONTS & TEXT
    public static Font STAT_FONT = new Font(
            "Sans-serif",
            Font.PLAIN,
            14);

    public static Font S_FONT = STAT_FONT.deriveFont((float) 16);

    // Sounds
    public static String START_MISS_ERR_SOUND   = "err1.wav";
    public static String TARGET_MISS_ERR_SOUND  = "err2.wav";
    public static String TARGET_HIT_SOUND       = "levelup.wav";
    public static String START_DOUBLE_ERR_SOUND = "err3.wav";

    // Log
    public static String SEP = ";";

}
