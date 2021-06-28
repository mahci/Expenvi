package envi.experiment;

import envi.connection.MooseServer;
import envi.gui.*;
import envi.tools.Configs;
import envi.tools.Strs;
import envi.tools.Utils;
import io.reactivex.rxjava3.subjects.PublishSubject;

import javax.swing.*;
import java.util.*;
import java.util.List;

public class Experimenter {

    private String TAG = "[[Experimenter]] ";
    private boolean toLog = false;

    private static Experimenter self = null; // for singleton
    //------------------------------------------------------------------------------
    // For publishing the state of the experiment
    private final PublishSubject<String> expSubject;

    // Warm-up or real experiment? (Only log if real experiment)
    public boolean realExperiment = false;

    // Timers
    private long homingStart = 0;

    //------------------------------------------------------------------------------
    private final int participantID = 2; // Participant's number

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

        // Send phase and technique to the Moose
        MooseServer.get().syncTechnique(techOrder[techInd]);

        // Start the phase
        switch (phase) {
        case SHOWCASE -> {
            System.out.println(TAG + "Showcase!");
            // Disable logging
            Mologger.get().setEnabled(false);
            // Show the start panel
            MainFrame.get().showPanel(
                    new StartPanel(PHASE.SHOWCASE, techOrder[techInd]));
            // Login the participant on the Moose
            MooseServer.get().syncParticipant(participantID);
        }
        case PRACTICE -> {
            // Enable logging
            Mologger.get().setEnabled(true);
            // Show the start panel
            MainFrame.get().showPanel(
                    new StartPanel(PHASE.PRACTICE, techOrder[techInd]));
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
