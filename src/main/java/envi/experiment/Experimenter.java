package envi.experiment;

import com.google.common.collect.ImmutableList;
import envi.gui.*;
import envi.tools.Utils;
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
    private boolean toLog = true;
    //==============================================================================
    private static Experimenter self = null; // for singleton

    // Display
    int winW, winH, dispW, dispH;

    // Vars
    private final List<Integer> radList   = new ArrayList<>();
    private final List<Integer> distList  = new ArrayList<>();
    private final List<Integer> dirList   = ImmutableList.of(0, 1); // 0: Left | 1: Right
    private final List<List<Integer>> expVarList = new ArrayList<>();

    // Experiment
    private final int participID = 1; // Participant's ID TODO: Convert to String?
    private final int currExpNum = 1; // Currently always 1 (for each participants

    // Blocks
    private final List<Block> blocks = new ArrayList<>();
    private int currBlockInd = 0;

    // Trials
    private int currTrialNum;

    // For publishing the state of the experiment
    private final PublishSubject<String> expSubject;

    // Warm-up or real experiment? (Only log if real experiment)
    public boolean realExperiment = false;

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

        // Save radii and distances in px values
        for(int rad: Config._targetRadiiMM) {
            radList.add(Utils.mm2px(rad));
        }
        if (toLog) System.out.println(TAG + "Rad list: " + radList);
        for(int dist: Config._distancesMM) {
            distList.add(Utils.mm2px(dist));
        }
        if (toLog) System.out.println(TAG + "Dist list: " + distList);
    }

    /***
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
        dispW = winW - (2 * Config._winWidthMargin);
        dispH = winH - (2 * Config._winHeightMargin);

        // participant starts
        if (realExperiment) {
            Mologger.get().logExpStart(
                    participID,
                    LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        }

        // Generate the combinations rad/dist/dir
        generateVarList();

        // Generate blocks
        blocks.clear();
        for (int bi = 0; bi < Config._nBlocksInExperiment; bi++) {
            blocks.add(new Block(TRIAL_TYPE.FITTS)
                            .setupFittsTrials(expVarList, dispW, dispH));
        }
        if (toLog) System.out.println(TAG + blocks.size() + " blocks created");

        // Publish the start of the experiment
        if (Config._interaction != Config.INTERACTION.MOUSE_LCLICK) {
            expSubject.onNext(Config.MSSG_BEG_EXP + "_" + Config._interaction + "--" + LocalDateTime.now());
        }

        // Run the first block
        currBlockInd = 0;
        startBlock(currBlockInd);
    }

    /**
     * Start a block
     * @param blkInd Block index
     */
    private void startBlock(int blkInd) {
        int blkNum = currBlockInd + 1;

        // Log the block start
        Mologger.get().logBlockStart(blkNum, LocalTime.now());

        // Publish
        expSubject.onNext(Config.MSSG_BEG_BLK + "_" + blkNum);

        // Run the block
        currTrialNum = 1;
        blocks.get(blkInd).setCurrTrialInd(-1); // TODO: is it needed?
        FittsTrial ftr = blocks.get(blkInd).getNextTrial(true);
        if (ftr != null) runFittsTrial(ftr);
        else System.out.println(TAG + "Problem in loading the trials! Block #" + blkInd);
    }

    /**
     * Trial is done
     * @param wasSuccess Was it successful?
     */
    public void trialDone(boolean wasSuccess) {
        // Go to the next trial
        FittsTrial ftr = blocks.get(currBlockInd).getNextTrial(wasSuccess);
        if (ftr != null) {
            if (toLog) System.out.println(TAG + "Next trial");
            // Log the end of the current trial
            Mologger.get().logTrialEnd();

            // Publish
            expSubject.onNext(Config.MSSG_END_TRL);

            // Run the next trial
            currTrialNum++;
            runFittsTrial(ftr);
        }
        else { // Trials in the block finished
            if (toLog) System.out.println(TAG + "Trials finished");
            // Log the end of the block
            Mologger.get().logBlockEnd(LocalTime.now());

            // Publish
            expSubject.onNext(Config.MSSG_END_BLK);

            // Are there more blocks?
            if (currBlockInd + 1 == blocks.size()) { // Blocks finished
                if (toLog) System.out.println(TAG + "All blocks finished");
                if (!realExperiment) { // Warm-up is finished
                    MainFrame.get().showPanel(new StartPanel(Config.PROCESS_STATE.EXPERIMENT));
                } else { // Experiment is finished
                    // Publish
                    expSubject.onNext(Config.MSSG_END_EXP);

                    showEnd();
                }
//            System.exit(0);
            } else { // There are more blocks
                // Show the break dialog
                showBreak();
            }
        }
    }

    /**
     * Run a Fitts trial
     * @param ftr Fitts trial
     */
    private void runFittsTrial(FittsTrial ftr) {

        // Create circles (translate the display area to appropriate location)
        Circle stacle = new Circle(
                Utils.dispToWin(ftr.getStaclePosition()),
                Config._stacleRad);
        Circle tarcle = new Circle(
                Utils.dispToWin(ftr.getTarclePosition()),
                ftr.getTarRad());
        if(toLog) System.out.println(TAG + "Stacle: " + stacle);
        if(toLog) System.out.println(TAG + "Tarcle: " + tarcle);

        //-- Create and send the panel to be drawn
        ExperimentPanel expPanel = new ExperimentPanel();
        expPanel.setCircles(stacle, tarcle);

        // Set texts
        String trlStat = "Trial: " + currTrialNum;
        int blockNum = currBlockInd + 1;
        String blkStat = "Block: " + blockNum + " / " + blocks.size();
        expPanel.setStatTexts(blkStat, trlStat);

        // Send to be shown!
        MainFrame.get().showPanel(expPanel);
    }

    /**
     * Show a break (between blocks)
     */
    public void showBreak() {
        expSubject.onNext(Config.MSSG_END_LOG);

        // Break dialog
        MainFrame.get().showDialog(new BreakDialog());
    }

    /**
     * Start the next block
     */
    public void nextBlock() {
        currBlockInd++;
        startBlock(currBlockInd);
    }


    /**
     * Generate all the combinations of radii, distances, directions
     */
    private void generateVarList() {
        // Generate all the pairs of radius/distance (using Point for int,int)
        for (int rad: radList) {
            for (int dist: distList) {
                for (int dir: dirList) {
                    expVarList.add(ImmutableList.of(rad, dist, dir));
                }
            }
        }
    }

    private void showEnd() {
        int input = JOptionPane.showOptionDialog(
                MainFrame.get(),
                "Experiment FINISHED! Thank You!",
                "THE END!",
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                null,
                null);
        if(input == JOptionPane.OK_OPTION)
        {
            System.exit(0);
        }
    }

    /**
     * Get the participant's ID
     * @return Participant's ID
     */
    public int getPID() {
        return participID;
    }

    /**
     * Get the PublishSubject to subscribe to
     * @return exPublishSubject
     */
    public PublishSubject<String> getExpSubject() {
        expSubject.subscribe(System.out::println);
        return expSubject;
    }
}
