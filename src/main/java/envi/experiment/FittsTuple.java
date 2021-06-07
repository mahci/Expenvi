package envi.experiment;

import envi.tools.Config;
import envi.tools.Utils;

import java.util.Collections;

/**
 * FittsTuple class to hold one combination of values
 */
public class FittsTuple {
    public int width; // Target width
    public int dist; // Target Distance
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
    public FittsTuple(int w, int d, int lr) {
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
        result.width = Utils.randInt(
                Collections.min(Config._widthsMM),
                Collections.max(Config._widthsMM) + 1);
        result.dist = Utils.randInt(
                Collections.min(Config._distancesMM),
                Collections.max(Config._distancesMM) + 1);
        result.leftRight = Utils.randInt(0, 2);

        return result;
    }

    @Override
    public String toString() {
        return "FittsTuple{" +
                "width=" + width +
                ", dist=" + dist +
                ", leftRight=" + leftRight +
                '}';
    }
}