package envi.log;

import envi.experiment.FittsTuple;
import envi.tools.Prefs;

public class InstantsLogInfo {
    public long trialShowInst;
    public long startPressInst;
    public long startReleaseInst;
    public long startCancelInst;
    public long startExitInst;
    public long targetFirstEntryInst;
    public long targetLastEntryInst;
    public long targetPressInst;
    public long targetReleaseInst;
    public long targetCancelInst;

    /**
     * Get the header for the log file
     * @return String - header with the names of the vars
     */
    public static String getLogHeader() {
        return "trial_show_inst" + Prefs.SEP +
                "start_press_inst" + Prefs.SEP +
                "start_release_inst" + Prefs.SEP +
                "start_cancel_inst" + Prefs.SEP +
                "start_exit_inst" + Prefs.SEP +
                "target_firstEntry_inst" + Prefs.SEP +
                "target_lastEntry_inst" + Prefs.SEP +
                "target_press_inst" + Prefs.SEP +
                "target_release_inst" + Prefs.SEP +
                "target_cancel_inst";
    }
    
    /**
     * Get the String of this object for logging
     * @return String - ';'-delimited
     */
    public String toLogString() {
        return trialShowInst + Prefs.SEP +
        startPressInst + Prefs.SEP +
        startReleaseInst + Prefs.SEP +
        startCancelInst + Prefs.SEP +
        startExitInst + Prefs.SEP +
        targetFirstEntryInst + Prefs.SEP +
        targetLastEntryInst + Prefs.SEP +
        targetPressInst + Prefs.SEP +
        targetReleaseInst + Prefs.SEP +
        targetCancelInst;
    }

}
