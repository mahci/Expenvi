package envi.experiment;

import com.google.common.collect.ImmutableList;
import envi.connection.MooseServer;
import envi.gui.*;
import envi.tools.Pair;
import envi.tools.Strs;
import envi.tools.Config;
import io.reactivex.rxjava3.subjects.PublishSubject;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class Experimenter {

    private String TAG = "[[Experimenter]] ";
    private boolean toLog = false;

    private static Experimenter self = null; // for singleton
    //------------------------------------------------------------------------------

    // Display
    int winW, winH, dispW, dispH;

    // Vars
    private final List<Integer> radList   = new ArrayList<>(); // list of radii in px
    private final List<Integer> distList  = new ArrayList<>(); // list of dists in px
    private final List<Integer> dirList   = ImmutableList.of(0, 1); // 0: Left | 1: Right
    private final List<List<Integer>> expVarList = new ArrayList<>();

    // Blocks
    private final List<Block> blocks = new ArrayList<>();
    private int currBlockInd = 0;

    // Trials
    private int currTrialNum;

    // For publishing the state of the experiment
    private final PublishSubject<String> expSubject;

    // Warm-up or real experiment? (Only log if real experiment)
    public boolean realExperiment = false;

    // Timers
    private long homingStart = 0;

    //------------------------------------------------------------------------------
    private final int participant = 6; // Participant's number

    private List<Config.TECH[]> techOrderList = new ArrayList<>();
    private Config.TECH[] techOrder;

    public enum PHASE {
        SHOWCASE,
        PRACTICE,
        EXPERIMENT
    }
    private PHASE phase;

    private int techInd = 0;

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

        // set the config from file
        Config.setFromFile();

        // Generate the order of the techniques
        genTechOrder();

        // Save radii and distances in px values
//        for(int rad: Config._targetRadiiMM) {
//            radList.add(Utils.mm2px(rad));
//        }
//        if (toLog) System.out.println(TAG + "Rad list: " + radList);
//        for(int dist: Config._distancesMM) {
//            distList.add(Utils.mm2px(dist));
//        }
//        if (toLog) System.out.println(TAG + "Dist list: " + distList);
    }

    public void genTechOrder() {
        // Setting the techniques orders
        techOrderList.add(new Config.TECH[] {Config.TECH.TAP, Config.TECH.MOUSE, Config.TECH.SWIPE});
        techOrderList.add(new Config.TECH[] {Config.TECH.SWIPE, Config.TECH.TAP, Config.TECH.MOUSE});
        techOrderList.add(new Config.TECH[] {Config.TECH.MOUSE, Config.TECH.SWIPE, Config.TECH.TAP});
        techOrderList.add(new Config.TECH[] {Config.TECH.TAP, Config.TECH.SWIPE, Config.TECH.MOUSE});
        techOrderList.add(new Config.TECH[] {Config.TECH.MOUSE, Config.TECH.TAP, Config.TECH.SWIPE});
        techOrderList.add(new Config.TECH[] {Config.TECH.SWIPE, Config.TECH.MOUSE, Config.TECH.TAP});

        // Get the order for the participant
        techOrder = techOrderList.get(participant % 6);
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
     * Get the technique from an index
     * @param ind Index
     * @return Technique
     */
    public String getTech(int ind) {
        if (ind < 3) return techOrder[ind].toString();
        else return "";
    }

    /**
     * Start a phase
     * @param phase PHASE
     */
    public void start(PHASE phase) {
        this.phase = phase;
        MooseServer.get().updateTechnique(techOrder[techInd]);

        switch (phase) {
        case SHOWCASE -> {
            // Disable logging
            Mologger.get().setEnabled(false);
            // Show the start panel
            MainFrame.get().showPanel(
                    new StartPanel(PHASE.SHOWCASE, techOrder[techInd]));
        }
        case PRACTICE -> {
            // Disable logging
            Mologger.get().setEnabled(false);
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
                MooseServer.get().sendMssg(Strs.MSSG_END_EXP);
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

    public Config.TECH getTechnique() {
        return techOrder[techInd];
    }

    public PHASE getPhase() {
        return phase;
    }

    public void setTechInd(int newInd) {
        techInd = newInd;
    }

    /**
     * Generate the list of blocks of the trials
     */
    private void generateBlocks() {
        // Create all combinations
        List<Pair<Integer, Integer>> allRadDists = new ArrayList<>();
        for (int rad: radList) {
            for (int dist: distList) {
                allRadDists.add(new Pair<>(rad, dist));
            }
        }

        // Create the 4 pairs of blocks (P1, P2, P3, P4)
        blocks.clear();
        for(int pi = 1; pi <= 4; pi++) {
            Collections.shuffle(allRadDists); // Shuffle the combinations

            // Init blocks
            Block b1 = new Block(TRIAL_TYPE.FITTS);
            Block b2 = new Block(TRIAL_TYPE.FITTS);

            // Even ind x R => B1 | Even ind x L => B2
            // Odd ind x R => B2 | Odd ind x L => B1
//            for(int ti = 0; ti < allRadDists.size(); ti++) {
//                if (ti % 2 == 0) { // Even
//                    b1.addTrial(new FittsTrial(allRadDists.get(ti), 1));
//                    b2.addTrial(new FittsTrial(allRadDists.get(ti), 0));
//                } else { // Odd
//                    b2.addTrial(new FittsTrial(allRadDists.get(ti), 1));
//                    b1.addTrial(new FittsTrial(allRadDists.get(ti), 0));
//                }
//            }

            // Add the blocks to the list of blocks
            blocks.add(b1.shuffle());
            blocks.add(b2.shuffle());

        }
    }



    /**
     * Start the experiment
     */
    public void startExperiment(boolean isRealExperiment) {
        if (toLog) System.out.println(TAG + "Experiment started");

        // Set the state
        realExperiment = isRealExperiment;
        if (toLog) System.out.println(TAG + "Real Exp? " + realExperiment);

        // Set the display size
        winW = MainFrame.get().getBounds().width;
        winH = MainFrame.get().getBounds().height;
//        dispW = winW - (2 * Config._winWidthMargin);
//        dispH = winH - (2 * Config._winHeightMargin);

        // participant starts
        if (realExperiment) {

        }

        // Generate the combinations rad/dist/dir
//        generateVarList();

        // Generate blocks
        generateBlocks();
//        blocks.clear();
//        for (int bi = 0; bi < Config._nBlocksInExperiment; bi++) {
//            blocks.add(new Block(TRIAL_TYPE.FITTS)
//                            .setupFittsTrials(expVarList, dispW, dispH));
//        }
//        if (toLog) System.out.println(TAG + blocks.size() + " blocks created");

        // Publish the start of the experiment
//        if (Config._technique != Config.TECH.MOUSE) {
//            LocalDateTime startTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
//            expSubject.onNext(Strs.MSSG_BEG_EXP + "-" + Config._technique + "--" + startTime);
//        }

        // Run the first block
        currBlockInd = 0;
//        startBlock(currBlockInd);
    }

    // -------------------------------------------------------------------------------

    //region [Getters & Setters]
    /**
     * Set the homing start time
     * @param t Long time
     */
    public void setHomingStart(long t) {
        homingStart = t;
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
        return participant;
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
