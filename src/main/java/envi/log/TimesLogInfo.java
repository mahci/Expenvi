package envi.log;

import envi.experiment.FittsTuple;
import envi.tools.Prefs;

import java.util.ArrayList;
import java.util.List;

public class TimesLogInfo {
//    public List<Integer> trialTimes = new ArrayList<>(); // List of trial times for each subblock
    public int trialTime;
    public int subblockTime;
    public int homingTime;
    public int phaseTime;
    public int experimentTime;

    /**
     * Get the header for the log file
     * @return String - header with the names of the vars
     */
    public static String getLogHeader() {
        return "trial_time" + Prefs.SEP +
                "subblock_time" + Prefs.SEP +
                "homing_time" + Prefs.SEP +
                "phase_time" + Prefs.SEP +
                "experiment_time";
    }
    
    /**
     * Get the String of this object for logging
     * @return String - ';'-delimited
     */
    public String toLogString() {
        return trialTime + Prefs.SEP +
                subblockTime + Prefs.SEP +
                homingTime + Prefs.SEP +
                phaseTime + Prefs.SEP +
                experimentTime;
    }
}
