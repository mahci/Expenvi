package envi.experiment;

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

    @Override
    public String toString() {
        return "FittsTuple{" +
                "width=" + width +
                ", dist=" + dist +
                ", leftRight=" + leftRight +
                '}';
    }
}
