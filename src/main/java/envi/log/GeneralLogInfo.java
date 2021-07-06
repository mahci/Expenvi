package envi.log;

import envi.experiment.Experimenter;
import envi.tools.Configs;
import envi.tools.Prefs;

public class GeneralLogInfo {
    public Configs.TECH technique;
    public Experimenter.PHASE phase;
    public int subBlockNum;
    public int trialNum;

    /**
     * Get the header for the log file
     * @return String - header with the names of the vars
     */
    public static String getLogHeader() {
        return "technique" + Prefs.SEP +
                "phase" + Prefs.SEP +
                "subblock_num" + Prefs.SEP +
                "trial_num";
    }

    /**
     * Get the String of this object for logging
     * @return String - ';'-delimited
     */
    public String toLogString() {
        return technique.ordinal() + Prefs.SEP +
                phase.ordinal() + Prefs.SEP +
                subBlockNum + Prefs.SEP +
                trialNum;
    }
}
