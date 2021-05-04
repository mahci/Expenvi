package envi.tools;

import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Utils {

    // Methods =============================================
    private static final double MM_IN_INCH = 25.4;

    /**
     * Convert mm to pixels
     * @param mm Millimeters
     * @return Pixels
     */
    public static int mm2px(int mm) {
//        int DPI = java.awt.Toolkit.getDefaultToolkit().getScreenResolution();
        return (int)((mm / MM_IN_INCH) * Config._dpi);
    }

    /**
     * Convert pixels to mm
     * @param px Pixels
     * @return Millimeters
     */
    public static int px2mm(int px) {
        return (int)((px / Config._dpi) * MM_IN_INCH);
    }

    /**
     * Do the mm2px conversion for all the required px values
     */
    public static void setPxValues() {
        Config._stacleRad = mm2px(Config._stacleRadMM);
    }

    /**
     * Get the int values from a String with a delimiter
     * @param src String source
     * @param del String delimiter
     * @return List<Integer>
     */
    public static List<Integer> intValues(String src, String del) {
        List<Integer> result = new ArrayList<>();

        if (!src.isEmpty()) {
            src = src.replaceAll("\\s", "");
            String[] strs = src.split(del);
            for(String str: strs) {
                result.add(Integer.valueOf(str));
            }
        }

        return result;
    }

    /**
     * Get the string of gestures
     * @param interaction Config.GESTURe
     * @return String
     */
    public static String interactionString(Config.INTERACTION interaction) {
        String result = "";
        switch (interaction) {
        case SWIPE_LCLICK:
            result = "SWIPE_LCLICK";
            break;
        case TAP_LCLICK:
            result = "TAP_LCLICK";
            break;
        case MOUSE_LCLICK:
            result = "MOUSE_LCLICK";
            break;
        }
        return result;
    }

    /**
     * Display area coordinates --> Windows coordinates
     * @param inPoint Point in display area
     * @return Window's coordinates
     */
    public static Point dispToWin(Point inPoint) {
        inPoint.translate(Config._winWidthMargin, Config._winHeightMargin);
        return inPoint;
    }

    /**
     * Get the last part of the input String (split by SPACE)
     * @param inStr Input String
     * @return String last part
     */
    public static String lastPart(String inStr) {
        if (!Objects.equals(inStr, "")) {
            String[] parts = inStr.split(" ");
            return parts[parts.length - 1];
        } else {
            return "";
        }
    }

    /**
     * Get the time in millis
     * @return Long timestamp
     */
    public static long now() {
        return Calendar.getInstance().getTimeInMillis();
    }
}
