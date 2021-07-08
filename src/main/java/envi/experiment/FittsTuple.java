package envi.experiment;

import envi.tools.Configs;
import envi.tools.Prefs;
import envi.tools.Utils;

import java.util.Collections;

/**
 * FittsTuple class to hold one combination of values
 */
public class FittsTuple {
    public double width; // Target width (mm)
    public double dist; // Target Distance (mm)
    public int leftRight; // Left = 0 / Right = 1
    //==============================================================================

    /**
     * Constructor (empty)
     */
    public FittsTuple() {

    }

    /**
     * Consturctor
     * @param w Width
     * @param d Distance
     * @param lr LeftRight
     */
    public FittsTuple(double w, double d, int lr) {
        this.width = w;
        this.dist = d;
        this.leftRight = lr;
    }

    /**
     * Generate a random FittsTuple
     * @return FittsTuple
     */
    public static FittsTuple randFittsTuple() {
        FittsTuple result = new FittsTuple();
        result.width = Utils.randDouble(
                Collections.min(Configs._widthsMM),
                Collections.max(Configs._widthsMM) + 1);
        result.dist = Utils.randDouble(
                Collections.min(Configs._distancesMM),
                Collections.max(Configs._distancesMM) + 1);
        result.leftRight = Utils.randInt(0, 2);

        return result;
    }

    /**
     * Get the header for logging
     * @return String
     */
    public static String getLogHeader() {
        return "width" + Prefs.DELIM +
                "dist" + Prefs.DELIM +
                "l_r";
    }

    @Override
    public String toString() {
        return "FittsTuple{" +
                "width=" + width +
                ", dist=" + dist +
                ", leftRight=" + leftRight +
                '}';
    }

    /**
     * Get the String for logging
     * @return String
     */
    public String toLogString() {
        return width + Prefs.DELIM +
                dist + Prefs.DELIM +
                leftRight + Prefs.DELIM;
    }
}
