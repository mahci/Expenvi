package envi.experiment;

import envi.Config;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class for the Fitt's experiment
 */
public class FittsTrial extends Trial {

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

        // Set the limits for choosing the STACLE center position
        int minX = 0, minY = 0, maxX = 0, maxY = 0;
        int maxRad = Math.max(tarRad, Config.STACLE_RAD); // Max radius between target or start
        if (leftRight == 0) { // Target on the left side
            minX = Config.WIN_HOR_MARGIN + (2 * tarRad) + tarDist + Config.STACLE_RAD;
            maxX = winW - (Config.WIN_HOR_MARGIN + Config.STACLE_RAD);

            minY = Config.WIN_VER_MARGIN + maxRad;
            maxY = winH - (Config.WIN_VER_MARGIN + maxRad);
        } else if (leftRight == 1) { // Target on the right
            minX = Config.WIN_HOR_MARGIN + Config.STACLE_RAD;
            maxX = winW - (Config.WIN_HOR_MARGIN + 2*tarRad + tarDist + Config.STACLE_RAD);

            minY = Config.WIN_VER_MARGIN + maxRad;
            maxY = winH - (Config.WIN_VER_MARGIN + maxRad);
        }

        // Determine a random position inside the boundaries
        staclePosition.x = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
        staclePosition.y = ThreadLocalRandom.current().nextInt(minY, maxY + 1);

        // Choose the position of the target based on the position of the stacle
        tarclePosition.y = staclePosition.y; // Always on the same Y
        if (leftRight == 0) tarclePosition.x = staclePosition.x - tarDist; // Left
        if (leftRight == 1) tarclePosition.x = staclePosition.x + tarDist; // Right
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
