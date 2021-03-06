package envi.experiment;

import envi.connection.MooseServer;
import envi.gui.*;
import envi.log.GeneralLogInfo;
import envi.log.Mologger;
import envi.log.TimesLogInfo;
import envi.tools.Configs;
import envi.tools.Strs;
import envi.tools.Utils;
import io.reactivex.rxjava3.subjects.PublishSubject;

import javax.swing.*;
import java.util.*;
import java.util.List;

public class Experimenter {

    private String TAG = "[[Experimenter]] ";
    private boolean toLog = true;

    private static Experimenter self = null; // for singleton
    //------------------------------------------------------------------------------
    // For publishing the state of the experiment
    private final PublishSubject<String> expSubject;

    // Timers
    private long homingStart = 0;

    // Log
    private GeneralLogInfo mGeneralLogInfo = new GeneralLogInfo();
    private TimesLogInfo mTimesLogInfo = new TimesLogInfo();
    private long phaseStartTime;
    private long expStartTime;

    //------------------------------------------------------------------------------
    private final int participantID = 0; // Participant's number

    // Techniques
    private List<Configs.TECH[]> techOrderList = new ArrayList<>();
    private Configs.TECH[] techOrder;
    private int techInd = 0;

    // Phases
    public enum PHASE {
        SHOWCASE,
        PRACTICE,
        EXPERIMENT
    }
    private PHASE phase;

    //==============================================================================

    /**
     * Get the instance
     * @return the singleton instance
     */
    public static Experimenter get() {
        if (self == null) self = new Experimenter();
        return self;
    }

    /**
     * Constructor
     */
    private Experimenter() {
        expSubject = PublishSubject.create();

        Configs.setFromFile(); // Set the config from file
        genTechOrder(); // Generate the order of the techniques
        // Start with the logging
        Mologger.get().logParticipant(participantID)
                .check();
    }

    public void genTechOrder() {
        // Setting the techniques orders
        techOrderList.add(new Configs.TECH[] {Configs.TECH.TAP, Configs.TECH.MOUSE, Configs.TECH.SWIPE});
        techOrderList.add(new Configs.TECH[] {Configs.TECH.SWIPE, Configs.TECH.TAP, Configs.TECH.MOUSE});
        techOrderList.add(new Configs.TECH[] {Configs.TECH.MOUSE, Configs.TECH.SWIPE, Configs.TECH.TAP});
        techOrderList.add(new Configs.TECH[] {Configs.TECH.TAP, Configs.TECH.SWIPE, Configs.TECH.MOUSE});
        techOrderList.add(new Configs.TECH[] {Configs.TECH.MOUSE, Configs.TECH.TAP, Configs.TECH.SWIPE});
        techOrderList.add(new Configs.TECH[] {Configs.TECH.SWIPE, Configs.TECH.MOUSE, Configs.TECH.TAP});

        // Get the order for the participant
        techOrder = techOrderList.get(participantID % 6);
    }

    /**
     * Get the current order of techniques
     * @return String
     */
    public String getTechOrderStr() {
        return "[1] " + techOrder[0] + "\t" +
                "[2] " + techOrder[1] + "\t" +
                "[3] " + techOrder[2];
    }

    /**
     * Start a phase
     * @param phase PHASE
     */
    public void start(PHASE phase) {
        this.phase = phase;
        if (toLog) System.out.println(TAG + "Start Phase: " + phase.name());

        MooseServer.get().syncExpLogId(); // Sync the exp to the Moose (if it's out of sync)
        MooseServer.get().syncTechnique(techOrder[techInd]); // Sync the technique

        // Save the log info
        mGeneralLogInfo.technique = techOrder[techInd];
        mGeneralLogInfo.phase = phase;
        phaseStartTime = Utils.nowInMillis();

        // Start the phase
        switch (phase) {
        case SHOWCASE -> {
            System.out.println(TAG + "Showcase!");
            // Disable logging
            Mologger.get().setEnabled(false);
            // Show the start panel
            MainFrame.get().showPanel(
                    new StartPanel(PHASE.SHOWCASE, techOrder[techInd]));
        }
        case PRACTICE -> {
            // Enable logging
            Mologger.get().setEnabled(true);
            // Show the start panel
            MainFrame.get().showPanel(
                    new StartPanel(PHASE.PRACTICE, techOrder[techInd]));

            // Start timing the experiment
            if (techInd == 0) expStartTime = Utils.nowInMillis();
        }
        case EXPERIMENT -> {
            // Enable logging
            Mologger.get().setEnabled(true);
            // Show the start panel
            MainFrame.get().showPanel(
                    new StartPanel(PHASE.EXPERIMENT, techOrder[techInd]));
        }
        }

    }

    /**
     * End of a phase
     * @param phase PHASE
     */
    public void end(PHASE phase) {
        if (toLog) System.out.println(TAG + "End Phase: " + phase.name());
        // Log the phase
        mTimesLogInfo.phaseTime = (int) (Utils.nowInMillis() - phaseStartTime);
        Mologger.get().logTime(mGeneralLogInfo, mTimesLogInfo);

        // Next phase?
        switch (phase) {
        case SHOWCASE -> {
            // Start from the first technique for this participant
            techInd = 0;
            MainFrame.get().removePanels();
            start(PHASE.PRACTICE);
        }
        case PRACTICE -> {
            start(PHASE.EXPERIMENT);
        }
        case EXPERIMENT -> {
            if (techInd < 2) { // Still techniques left to experiment
                techInd++;
                start(PHASE.PRACTICE); // Start with the next technique
            } else { // All the techniques are tested
                // Tell teh Moose about the end of the experiment
                MooseServer.get().sendMssg(Strs.MSSG_END_EXP, "");
                // Save log info
                mTimesLogInfo.experimentTime = (int) (Utils.nowInMillis() - expStartTime) / 1000; // in seconds
                Mologger.get().logTime(mGeneralLogInfo, mTimesLogInfo);

                // Finish alll the logs
                Mologger.get().closeLogs();

                // Show the end dialog
                endDialog();

            }
        }
        }
    }

    /**
     * The current phase is ended
     */
    public void endPhase() {
        end(phase);
    }

    /**
     * Show the end dialog
     */
    private void endDialog() {
        int input = JOptionPane.showOptionDialog(
                MainFrame.get(),
                Strs.DLG_END_TEXT,
                Strs.DLG_END_TITLE,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                null,
                null);
        if(input == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    public Configs.TECH getTechnique() {
        return techOrder[techInd];
    }

    /**
     * Return the ordinal of the current technique
     * @return int
     */
    public int getTechOrdinal() {
        return techOrder[techInd].ordinal();
    }

    /**
     * Return the ordinal of the current phase
     * @return int
     */
    public int getPhaseOrdinal() {
        return phase.ordinal();
    }

    /**
     * Get the technique from an index
     * @param ind Index
     * @return Technique
     */
    public String getTech(int ind) {
        if (ind < 3) return techOrder[ind].toString();
        else return "";
    }

    /**
     * Check the current technique against a given technique
     * @param tech TECH to check
     * @return Boolean
     */
    public boolean isTechnique(Configs.TECH tech) {
        return Objects.equals(tech, techOrder[techInd]);
    }

    public PHASE getPhase() {
        return phase;
    }

    public void setTechInd(int newInd) {
        techInd = newInd;
    }

    // -------------------------------------------------------------------------------

    //region [Getters & Setters]
    /**
     * Set the homing start time
     */
    public void startHoming() {
        homingStart = Utils.nowInMillis();
    }

    /**
     * Reset the homing time
     */
    public void resetHoming() {
        homingStart = 0;
    }

    /**
     * Get the PublishSubject to subscribe to
     * @return exPublishSubject
     */
    public PublishSubject<String> getExpSubject() {
        expSubject.subscribe(System.out::println);
        return expSubject;
    }

    /**
     * Get the participant's ID
     * @return Participant's ID
     */
    public int getPID() {
        return participantID;
    }

    /**
     * Get the homing start time
     * @return Long time
     */
    public long getHomingStart() {
        return homingStart;
    }
    //endregion
}
