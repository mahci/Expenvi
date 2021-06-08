package envi.tools;

import javax.sound.sampled.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static final double MM_IN_INCH = 25.4;

    // ==============================================================================

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
     * @param TECH Config.GESTURe
     * @return String
     */
    public static String interactionString(Config.TECH TECH) {
        String result = "";
        switch (TECH) {
        case SWIPE:
            result = "SWIPE_LCLICK";
            break;
        case TAP:
            result = "TAP_LCLICK";
            break;
        case MOUSE:
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
    public static long nowInMillis() {
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

    /**
     * Get the current time up to the seconds
     * @return LocalTime
     */
    public static LocalTime nowTime() {
        return LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * Get the current date+time up to minutes
     * @return LocalDateTime
     */
    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    /**
     * Play a sound
     * @param resFileName File name in /resources folder
     */
    public static void playSound(String resFileName) {
        try {
            ClassLoader classLoader = Utils.class.getClassLoader();
            File soundFile = new File(classLoader.getResource(resFileName).getFile());
            URI uri = soundFile.toURI();
            URL url = uri.toURL();
            AudioClip clip = Applet.newAudioClip(url);
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundFile);
            clip.play();
        } catch ( UnsupportedAudioFileException
                | NullPointerException
                | IOException e
                ) {
            e.printStackTrace();
        }

    }
}
