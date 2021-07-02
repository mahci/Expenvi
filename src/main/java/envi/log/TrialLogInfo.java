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
        return FittsTuple.getLogHeader() + Prefs.SEP +
                "start_pos_x" + Prefs.SEP +
                "start_pos_y" + Prefs.SEP +

                "target_pos_x" + Prefs.SEP +
                "target_pos_y" + Prefs.SEP +

                "start_press_x" + Prefs.SEP +
                "start_press_y" + Prefs.SEP +
                "start_press_d" + Prefs.SEP +

                "start_release_x" + Prefs.SEP +
                "start_release_y" + Prefs.SEP +
                "start_release_d" + Prefs.SEP +

                "start_cancel_x" + Prefs.SEP +
                "start_cancel_y" + Prefs.SEP +
                "start_cancel_d" + Prefs.SEP +

                "start_exit_x" + Prefs.SEP +
                "start_exit_y" + Prefs.SEP +

                "n_target_entries" + Prefs.SEP +

                "target_press_x" + Prefs.SEP +
                "target_press_y" + Prefs.SEP +
                "target_press_d" + Prefs.SEP +

                "target_release_x" + Prefs.SEP +
                "target_release_y" + Prefs.SEP +
                "target_release_d" + Prefs.SEP +

                "target_cancel_x" + Prefs.SEP +
                "target_cancel_y" + Prefs.SEP +
                "target_cancel_d" + Prefs.SEP +

                "target_first_entry_x" + Prefs.SEP +
                "target_first_entry_y" + Prefs.SEP +
                "target_last_entry_x" + Prefs.SEP +
                "target_last_entry_y" + Prefs.SEP +

                "selection_time" + Prefs.SEP +
                "result";
    }

    /**
     * Get the String of this object for logging
     * @return String - ';'-delimited
     */
    public String toLogString() {
        return fittsTuple.toLogString() +
                pointToLogString(startPosition) + Prefs.SEP +
                pointToLogString(targetPosition) + Prefs.SEP +

                pointToLogString(startPressPoint) + Prefs.SEP +
                Utils.double3Dec(startPressDist) + Prefs.SEP +

                pointToLogString(startReleasePoint) + Prefs.SEP +
                Utils.double3Dec(startReleaseDist) + Prefs.SEP +

                pointToLogString(startCancelPoint) + Prefs.SEP +
                Utils.double3Dec(startCancelDist) + Prefs.SEP +

                pointToLogString(startExitPoint) + Prefs.SEP +

                nTargetEntries + Prefs.SEP +

                pointToLogString(targetPressPoint) + Prefs.SEP +
                Utils.double3Dec(targetPressDist) + Prefs.SEP +

                pointToLogString(targetReleasePoint) + Prefs.SEP +
                Utils.double3Dec(targetReleaseDist) + Prefs.SEP +

                pointToLogString(targetCancelPoint) + Prefs.SEP +
                Utils.double3Dec(targetCancelDist) + Prefs.SEP +

                pointToLogString(targetFirstEntyPoint) + Prefs.SEP +
                pointToLogString(targetLastEntyPoint) + Prefs.SEP +

                selectionTime + Prefs.SEP +
                result + Prefs.SEP;
    }

    /**
     * Get the String of a Point
     * @param p Point
     * @return String
     */
    private String pointToLogString(Point p) {
        return p.x + Prefs.SEP + p.y;
    }

}
