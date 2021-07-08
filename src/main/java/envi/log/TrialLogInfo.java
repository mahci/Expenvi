package envi.log;

import envi.experiment.FittsTuple;
import envi.tools.Prefs;
import envi.tools.Utils;

import java.awt.*;

public class TrialLogInfo {
    public FittsTuple fittsTuple = new FittsTuple();
    public Point startPosition = new Point();
    public Point targetPosition = new Point();

    public Point startPressPoint = new Point();
    public double startPressDist; // mm - distance to the center

    public Point startReleasePoint = new Point();
    public double startReleaseDist; // mm - distance to the center

    public Point startCancelPoint = new Point();
    public double startCancelDist; // mm - distance to the center

    public Point startExitPoint = new Point();

    public int nTargetEntries;

    public Point targetPressPoint = new Point();
    public double targetPressDist; // mm - distance to the center

    public Point targetReleasePoint = new Point();
    public double targetReleaseDist; // mm - distance to the center

    public Point targetCancelPoint = new Point();
    public double targetCancelDist; // mm - distance to the center

    public Point targetFirstEntyPoint = new Point();
    public Point targetLastEntyPoint = new Point();

    public int selectionTime; // ms - from START_release to TARGET_press
    public int result; // How did the trial ended?

    /**
     * Reset event fields
     */
    public void reset() {
        startPressPoint = new Point();
        startPressDist = 0.0;

        startReleasePoint = new Point();
        startReleaseDist = 0.0;

        startCancelPoint = new Point();
        startCancelDist = 0.0;

        startExitPoint = new Point();

        nTargetEntries = 0;

        targetPressPoint = new Point();
        targetPressDist = 0.0;

        targetReleasePoint = new Point();
        targetReleaseDist = 0.0;

        targetCancelPoint = new Point();
        targetCancelDist = 0.0;

        targetFirstEntyPoint = new Point();
        targetLastEntyPoint = new Point();

        selectionTime = 0;
        result = -2;
    }

    /**
     * Get the header for the log file
     * @return String - header with the names of the vars
     */
    public static String getLogHeader() {
        return FittsTuple.getLogHeader() + Prefs.DELIM +
                "start_pos_x" + Prefs.DELIM +
                "start_pos_y" + Prefs.DELIM +

                "target_pos_x" + Prefs.DELIM +
                "target_pos_y" + Prefs.DELIM +

                "start_press_x" + Prefs.DELIM +
                "start_press_y" + Prefs.DELIM +
                "start_press_d" + Prefs.DELIM +

                "start_release_x" + Prefs.DELIM +
                "start_release_y" + Prefs.DELIM +
                "start_release_d" + Prefs.DELIM +

                "start_cancel_x" + Prefs.DELIM +
                "start_cancel_y" + Prefs.DELIM +
                "start_cancel_d" + Prefs.DELIM +

                "start_exit_x" + Prefs.DELIM +
                "start_exit_y" + Prefs.DELIM +

                "n_target_entries" + Prefs.DELIM +

                "target_press_x" + Prefs.DELIM +
                "target_press_y" + Prefs.DELIM +
                "target_press_d" + Prefs.DELIM +

                "target_release_x" + Prefs.DELIM +
                "target_release_y" + Prefs.DELIM +
                "target_release_d" + Prefs.DELIM +

                "target_cancel_x" + Prefs.DELIM +
                "target_cancel_y" + Prefs.DELIM +
                "target_cancel_d" + Prefs.DELIM +

                "target_first_entry_x" + Prefs.DELIM +
                "target_first_entry_y" + Prefs.DELIM +
                "target_last_entry_x" + Prefs.DELIM +
                "target_last_entry_y" + Prefs.DELIM +

                "selection_time" + Prefs.DELIM +
                "result";
    }

    /**
     * Get the String of this object for logging
     * @return String - ';'-delimited
     */
    public String toLogString() {
        return fittsTuple.toLogString() +
                pointToLogString(startPosition) + Prefs.DELIM +
                pointToLogString(targetPosition) + Prefs.DELIM +

                pointToLogString(startPressPoint) + Prefs.DELIM +
                Utils.double3Dec(startPressDist) + Prefs.DELIM +

                pointToLogString(startReleasePoint) + Prefs.DELIM +
                Utils.double3Dec(startReleaseDist) + Prefs.DELIM +

                pointToLogString(startCancelPoint) + Prefs.DELIM +
                Utils.double3Dec(startCancelDist) + Prefs.DELIM +

                pointToLogString(startExitPoint) + Prefs.DELIM +

                nTargetEntries + Prefs.DELIM +

                pointToLogString(targetPressPoint) + Prefs.DELIM +
                Utils.double3Dec(targetPressDist) + Prefs.DELIM +

                pointToLogString(targetReleasePoint) + Prefs.DELIM +
                Utils.double3Dec(targetReleaseDist) + Prefs.DELIM +

                pointToLogString(targetCancelPoint) + Prefs.DELIM +
                Utils.double3Dec(targetCancelDist) + Prefs.DELIM +

                pointToLogString(targetFirstEntyPoint) + Prefs.DELIM +
                pointToLogString(targetLastEntyPoint) + Prefs.DELIM +

                selectionTime + Prefs.DELIM +
                result;
    }

    /**
     * Get the String of a Point
     * @param p Point
     * @return String
     */
    private String pointToLogString(Point p) {
        return p.x + Prefs.DELIM + p.y;
    }

}
