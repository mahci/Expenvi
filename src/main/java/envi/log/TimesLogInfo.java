package envi.log;

import envi.tools.Prefs;

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
        return "trial_time" + Prefs.DELIM +
                "subblock_time" + Prefs.DELIM +
                "homing_time" + Prefs.DELIM +
                "phase_time" + Prefs.DELIM +
                "experiment_time";
    }
    
    /**
     * Get the String of this object for logging
     * @return String - ';'-delimited
     */
    public String toLogString() {
        return trialTime + Prefs.DELIM +
                subblockTime + Prefs.DELIM +
                homingTime + Prefs.DELIM +
                phaseTime + Prefs.DELIM +
                experimentTime;
    }
}
