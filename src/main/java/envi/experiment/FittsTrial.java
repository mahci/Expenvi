package envi.experiment;

import envi.gui.MainFrame;
import envi.tools.Configs;
import envi.tools.Utils;

import java.awt.*;

/**
 * Class for the Fitt's experiment
 */
public class FittsTrial {

    private final String TAG = "[[FittsTrial]] ";
    private final boolean toLog = true;
    // -------------------------------------------------------------------------------

    // Positions of start and target circles (centers)
    private Point staclePosition = new Point();
    private Point tarclePosition = new Point();

    // Variables tuple
    public FittsTuple vars;

    // ===============================================================================

    /**
     * Generate a trial with a tuple
     * @param fTuple FittsTuple
     */
    public FittsTrial(FittsTuple fTuple) {
        this.vars = fTuple;
        positionMM();
    }

    /**
     * Set the positions of the circles
     */
    public void position() {
//        int dispW = MainFrame.get().getDispArea().first;
//        int dispH = MainFrame.get().getDispArea().second;
//
//        // Convert the value into px for calculations
//        int tarRad = Utils.mm2px(vars.width);
//        int tarDist = Utils.mm2px(vars.dist);
//
//        if (toLog) System.out.println(TAG + String.format("disp W = %d, H = %d", dispW, dispH));
//        //--- *STACLE* center position thresholds
//        int minX, minY, maxX, maxY;
//        int maxRad = Math.max(tarRad, Configs._stacleRad); // Max radius between target or start
//        if (toLog) System.out.println(TAG + String.format("maxRad = %d", maxRad));
//
//        // Y (independant of the left/right)
//        minY = maxRad;
//        maxY = dispH - maxRad;
//        if (toLog) System.out.println(TAG + String.format("Y -> min = %d, max = %d", minY, maxY));
//        staclePosition.y = ThreadLocalRandom.current().nextInt(minY, maxY + 1);
//        tarclePosition.y = staclePosition.y;
//
//        // X
//        if (vars.leftRight == 0) { // Left
//            minX = tarRad + tarDist;
//            maxX = dispW - Configs._stacleRad;
//            if (toLog) System.out.println(TAG + String.format("(Left) X -> min = %d, max = %d", minX, maxX));
//            // Determine a random position
//            staclePosition.x = Utils.randInt(minX, maxX + 1);
//            tarclePosition.x = staclePosition.x - tarDist;
//        } else { // Right
//            minX = Configs._stacleRad;
//            maxX = dispW - (tarRad + tarDist);
//            if (toLog) System.out.println("maxX -> " + dispW + " | " + tarRad + " , " + tarDist);
//            if (toLog) System.out.println(TAG + String.format("(Right) X -> min = %d, max = %d", minX, maxX));
//            // Determine a random position
//            staclePosition.x = Utils.randInt(minX, maxX + 1);
//            tarclePosition.x = staclePosition.x + tarDist;
//        }
//
//        if (toLog) System.out.println(TAG + "Stacle position: " + staclePosition);
//        if (toLog) System.out.println(TAG + "Tarcle position: " + tarclePosition);
    }

    private void positionMM() {
        int dispW = MainFrame.get().getDispArea().first;
        int dispH = MainFrame.get().getDispArea().second;

        int minX, minY, maxX, maxY;

        if (toLog) System.out.println(TAG + String.format("disp W = %d, H = %d", dispW, dispH));
        //--- *STACLE* center position thresholds
        int maxRad = Math.max(vars.width, Configs._stacleRadMM); // Max radius between target or start
        if (toLog) System.out.println(TAG + String.format("maxRad = %d", maxRad));

        // Y (independant of the left/right)
        minY = maxRad;
        maxY = dispH - maxRad;
        if (toLog) System.out.println(TAG + String.format("Y -> min = %d, max = %d", minY, maxY));
        staclePosition.y = Utils.randInt(minY, maxY + 1);
        tarclePosition.y = staclePosition.y;

        // X
        if (vars.leftRight == 0) { // Left
            minX = vars.width + vars.dist;
            maxX = dispW - Configs._stacleRad;
            if (toLog) System.out.println(TAG + String.format("(Left) X -> min = %d, max = %d", minX, maxX));
            // Determine a random position
            staclePosition.x = Utils.randInt(minX, maxX + 1);
            tarclePosition.x = staclePosition.x - vars.dist;
        } else { // Right
            minX = Configs._stacleRadMM;
            maxX = dispW - (vars.width + vars.dist);
//            if (toLog) System.out.println("maxX -> " + dispW + " | " + tarRad + " , " + tarDist);
            if (toLog) System.out.println(TAG + String.format("(Right) X -> min = %d, max = %d", minX, maxX));
            // Determine a random position
            staclePosition.x = Utils.randInt(minX, maxX + 1);
            tarclePosition.x = staclePosition.x + vars.dist;
        }

        if (toLog) System.out.println(TAG + "Stacle position: " + staclePosition);
        if (toLog) System.out.println(TAG + "Tarcle position: " + tarclePosition);
    }

    /**
     * Return start cicle position
     * @return Start circle center position
     */
    public Point getStaclePosition() { return staclePosition; }

    /**
     * Return target circle position
     * @return Target circle center
     */
    public Point getTarclePosition() { return tarclePosition; }

    /**
     * Get target radius (px)
     * @return int
     */
//    public int getTarRad() {
//        return Utils.mm2px(vars.width);
//    }

    public int getTarWidth() {
        return vars.width;
    }

    /**
     * Get the string of the FittsTrial
     * @return String of the parameters
     */
    @Override
    public String toString() {
        return "FittsTrial{" +
                "staclePosition=" + Utils.pointToString(staclePosition) +
                ", tarclePosition=" + Utils.pointToString(tarclePosition) +
                ", tarRad=" + vars.width +
                ", tarDist=" + vars.dist +
                ", tarDir=" + vars.leftRight +
                '}';
    }
}
