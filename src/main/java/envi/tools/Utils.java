package envi.tools;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static final double MM_IN_INCH = 25.4;

    // ===============================================================================





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
     * @param TECHNIQUE Config.GESTURe
     * @return String
     */
    public static String interactionString(Config.TECHNIQUE TECHNIQUE) {
        String result = "";
        switch (TECHNIQUE) {
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

    /**
     * Return a radnom int
     * @param min Minimum (inclusive)
     * @param bound Bound (exclusive)
     * @return Random int
     */
    public static int randInt(int min, int bound) {
        return ThreadLocalRandom.current().nextInt(min, bound);
    }

    /**
     * Generate a random permutation of {0, 1, ..., len - 1}
     * @param len Length of the permutation
     * @return Random permutation
     */
    public static List<Integer> randPerm(int len) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            indexes.add(i);
        }
        Collections.shuffle(indexes);

        return indexes;
    }
}
