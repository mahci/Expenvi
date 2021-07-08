package envi.log;

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
        return "trial_show_inst" + Prefs.DELIM +
                "start_press_inst" + Prefs.DELIM +
                "start_release_inst" + Prefs.DELIM +
                "start_cancel_inst" + Prefs.DELIM +
                "start_exit_inst" + Prefs.DELIM +
                "target_firstEntry_inst" + Prefs.DELIM +
                "target_lastEntry_inst" + Prefs.DELIM +
                "target_press_inst" + Prefs.DELIM +
                "target_release_inst" + Prefs.DELIM +
                "target_cancel_inst";
    }
    
    /**
     * Get the String of this object for logging
     * @return String - ';'-delimited
     */
    public String toLogString() {
        return trialShowInst + Prefs.DELIM +
        startPressInst + Prefs.DELIM +
        startReleaseInst + Prefs.DELIM +
        startCancelInst + Prefs.DELIM +
        startExitInst + Prefs.DELIM +
        targetFirstEntryInst + Prefs.DELIM +
        targetLastEntryInst + Prefs.DELIM +
        targetPressInst + Prefs.DELIM +
        targetReleaseInst + Prefs.DELIM +
        targetCancelInst;
    }

}
