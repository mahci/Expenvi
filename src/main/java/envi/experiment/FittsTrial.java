package envi.experiment;

import envi.tools.Config;
import envi.tools.Utils;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class for the Fitt's experiment
 */
public class FittsTrial extends Trial {

    private final String TAG = "[[FittsTrial]] ";
    private final boolean toLog = true;

    // Positions of start and target circles (centers)
    private Point staclePosition = new Point();
    private Point tarclePosition = new Point();

    // Vars
    private int tarRad;
    private int tarDist;
    private int tarDir;

    // Index
    private int trialNum;
    private int blockNum;

    /**
     * Create a trial
     * @param tarRad Target radius
     * @param tarDist Target distance from the center of the start circle
     * @param leftRight Target to the left/right of the start circle (left = 0 | rigth = 1)
     * @param winW Width of the window
     * @param winH Height of the window
     */
    public FittsTrial(int winW, int winH, int tarRad, int tarDist, int leftRight) {

        // Set the vars
        this.tarRad = tarRad;
        this.tarDist = tarDist;
        this.tarDir = leftRight;

        // Set the postions
        setPositions(winW, winH);
    }

    /**
     * Generate and return a random Fitts trial
     * @param dispW Width of the display area
     * @param dispH Height of the display area
     */
    public FittsTrial(int dispW, int dispH) {
        // Size of the display area
//        int dispAreaW = winW - (2 * Config.WIN_W_MARGIN);
//        int dispAreaH = winH - (2 * Config.WIN_H_MARGIN);

        //--- Random target radius
        int minTarRad = Utils.mm2px(Config._minTarRadMM);
        int maxTarRad = dispH / Config.DISP_H_RATIO_TAR_RAD;
        if (toLog) System.out.println(TAG +
            String.format("minTarRad = %d, max = %d", minTarRad, maxTarRad));

        this.tarRad = ThreadLocalRandom.current().nextInt(minTarRad, maxTarRad + 1);
        if (toLog) System.out.println(TAG + String.format("tarRad = %d", tarRad));
        //--- Random distance
        int minDist = Config._stacleRad + this.tarRad;
        int macDist = dispW - Config._stacleRad - this.tarRad;

        this.tarDist = ThreadLocalRandom.current().nextInt(minDist, macDist + 1);

        // Random direction
        if (new Random().nextBoolean()) this.tarDir = 1;

        // Set the positions
        setPositions(dispW, dispH);
    }

    /**
     * Set the positions of the circles based on other parameters (already set)
     * The position of STACLE is randomly chosen
     */
    private void setPositions(int dispW, int dispH) {
        if (toLog) System.out.println(TAG + String.format("disp W = %d, H = %d", dispW, dispH));
        //--- *STACLE* center position thresholds
        int minX, minY, maxX, maxY;
        int maxRad = Math.max(tarRad, Config._stacleRad); // Max radius between target or start
        if (toLog) System.out.println(TAG + String.format("maxRad = %d", maxRad));
        // Y is independant of the left/right
        minY = maxRad;
        maxY = dispH - maxRad;
        if (toLog) System.out.println(TAG + String.format("Y - min = %d, max = %d", minY, maxY));
        staclePosition.y = ThreadLocalRandom.current().nextInt(minY, maxY + 1);
        tarclePosition.y = staclePosition.y;

        // X
        if (tarDir == 0) {
            minX = tarRad + tarDist;
            maxX = dispW - Config._stacleRad;
            if (toLog) System.out.println(TAG + String.format("X - min = %d, max = %d", minX, maxX));
            // Determine a random position
            staclePosition.x = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
            tarclePosition.x = staclePosition.x - tarDist;
        } else {
            minX = Config._stacleRad;
            maxX = dispW - (tarRad + tarDist);
            if (toLog) System.out.println(TAG + String.format("X - min = %d, max = %d", minX, maxX));
            // Determine a random position
            staclePosition.x = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
            tarclePosition.x = staclePosition.x + tarDist;
        }

    }

    /**
     * Return target radius
     * @return Target radius
     */
    public int getTarRad() { return tarRad; }

    /**
     * Return target distance
     * @return Target distance
     */
    public int getTarDist() { return tarDist; }

    /**
     * Return target direction
     * @return Target direction
     */
    public int getTarDir() { return tarDir; }

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


}
