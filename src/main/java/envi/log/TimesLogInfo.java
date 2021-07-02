package envi.log;

import envi.tools.Prefs;

import java.util.ArrayList;
import java.util.List;

public class TimesLogInfo {
    public List<Integer> trialTimes = new ArrayList<>(); // List of trial times for each subblock
    public int subblockTime;
    public int homingTime;
    public int phaseTime;
    public int experimentTime;

    /**
     * Get the header for the log file
     * @return String - header with the names of the vars
     */
    public static String getLogHeader() {
        StringBuilder headerSB = new StringBuilder();

        for (int ti = 1; ti < 12; ti++) {
            headerSB.append("trial_").append(ti).append("_time").append(Prefs.SEP);
        }
        headerSB.append("subblock_time").append(Prefs.SEP)
                .append("homing_time").append(Prefs.SEP)
                .append("phase_time").append(Prefs.SEP)
                .append("experiment_time").append(Prefs.SEP);

        return headerSB.toString();
    }
    
    /**
     * Get the String of this object for logging
     * @return String - ';'-delimited
     */
    public String toLogString() {
        StringBuilder result = new StringBuilder();

        for (int ti = 0; ti < trialTimes.size(); ti++) {
            result.append(trialTimes.get(ti)).append(Prefs.SEP);
        }
        result.append(subblockTime).append(Prefs.SEP)
                .append(homingTime).append(Prefs.SEP)
                .append(phaseTime).append(Prefs.SEP)
                .append(experimentTime);

        return result.toString();
    }
}
