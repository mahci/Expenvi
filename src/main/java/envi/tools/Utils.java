package envi.tools;

import envi.gui.MainFrame;

import java.util.ArrayList;
import java.util.List;

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
        return (int)((mm / MM_IN_INCH) * Config.DPI);
    }

    /**
     * Convert pixels to mm
     * @param px Pixels
     * @return Millimeters
     */
    public static int px2mm(int px) {
        return (int)((px / Config.DPI) * MM_IN_INCH);
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
     * @param gesture Config.GESTURe
     * @return String
     */
    public static String actionString(Config.GESTURE gesture) {
        String result = "";
        switch (gesture) {
        case SWIPE_LCLICK:
            result = "SWIPE";
            break;
        case TAP_LCLICK:
            result = "TAP";
            break;
        }
        return result;
    }
}
