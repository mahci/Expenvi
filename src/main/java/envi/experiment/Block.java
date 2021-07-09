package envi.experiment;

import envi.tools.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/***
 * The class for the a block of trials
 */
public class Block {

    private String TAG = "[[Block]] ";
    private boolean toLog = false;
    // -------------------------------------------------------------------------------

    private ArrayList<FittsTrial> trials = new ArrayList<>(); // List of trials
    private TRIAL_TYPE trialsType;
    private int currTrialInd;

    private List<FittsTuple> combinations = new ArrayList<>(); // List of all the combination
    private List<SubBlock> subBlocks = new ArrayList<>(); // List of sub-blocks

    // ===============================================================================

    /**
     * Constructor
     * @param trialsType Type of the trial (only Fitts for now)
     */
    public Block(TRIAL_TYPE trialsType) {
        this.trialsType = trialsType;
    }

    /**
     * Create a block with the sets of
     */
    public Block(List<FittsTuple> combinations, int nSubBlocks) {

        List<FittsTuple> cmbList = new ArrayList<FittsTuple>(combinations);

        // Randomly put the combinations in trials into 3 sub-blocks
        if (nSubBlocks == 3) {
            Collections.shuffle(cmbList);

            int third = cmbList.size() / 3;

            if(toLog) System.out.println(TAG + third);
            subBlocks.add(new SubBlock(cmbList.subList(0, third)));
            subBlocks.add(new SubBlock(cmbList.subList(third, 2 * third)));
            subBlocks.add(new SubBlock(cmbList.subList(2 * third, cmbList.size())));
        }

        // Each L or R gets into one sub-block
        if (nSubBlocks == 2) {
            // Create and add two sub-blocks
            subBlocks.add(new SubBlock());
            subBlocks.add(new SubBlock());

            // Get a random permutation of {0, 1, ..., 11}
            List<Integer> indexes = Utils.randPerm(cmbList.size() / 2);

            // Randomly put L or R in Sb1 or SB2
            for (int ind : indexes) {
                List<Integer> dirs = Utils.randPerm(2);
                subBlocks.get(dirs.get(0)).addTrial(cmbList.get(ind * 2));
                subBlocks.get(dirs.get(1)).addTrial(cmbList.get(ind * 2 + 1));
            }
        }

    }

    /**
     * Initialize everything
     */
    public void init() {
        trials = new ArrayList<>();
        combinations = new ArrayList<>();
        subBlocks = new ArrayList<>();
    }

    /**
     * Get total number of sub-blocks
     * @return
     */
    public int getNSubBlocks() {
        return subBlocks.size();
    }

    public boolean hasNext(int sbNum) {
        return subBlocks.size() - sbNum > 0;
    }

    /**
     * Add a trial to the list
     * @param ft FittsTrial
     */
    public void addTrial(FittsTrial ft) {
        trials.add(ft);
    }

    /**
     * Shuffle the trials
     * @return self
     */
    public Block shuffle() {
        Collections.shuffle(trials);
        return this;
    }

    /**
     * Output the next trial
     * @param wasSuccess Was the previous trial successful?
     * @return The next trial
     */
    public FittsTrial getNextTrial(boolean wasSuccess) {
        currTrialInd++;

        // [REPLACED]
        // Success => normal return
        // Fail => do the procedure
        //            trialMiss(currTrialInd);

        if (currTrialInd < trials.size()) return trials.get(currTrialInd);
        else return null; // Should always be a trial!
    }

    public SubBlock getSubBlock(int sbNum) {
        if (sbNum <= subBlocks.size()) return subBlocks.get(sbNum - 1);
        else return null;
    }

    /**
     * Get a trial from index
     * @param trInd Trial index
     * @return FittsTrial
     */
    public FittsTrial getTrial(int trInd) {
        System.out.println(TAG + "Number of trials = " + trials.size());
        if (trInd < trials.size()) return trials.get(trInd);
        else return null;
    }

    /**
     * Trial of @trialInd was failed => shuffle it to another future palce
     * @param trialInd Index of the trial (from 1)
     */
    public void trialMiss(int trialInd) {
        int fi = trialInd - 1;
        FittsTrial failedTrial = trials.get(fi);

        // Get the rest of the elments (if trial is the last one, should return empty)
        ArrayList<FittsTrial> restList = new ArrayList<>(trials.subList(fi + 1, trials.size()));

        // Clear the rest of the trials and put in the new made list
        trials.subList(fi + 1, trials.size()).clear();
        trials.addAll(fi + 1, shuffleIn(restList, failedTrial));
    }

    /**
     * Shuffle a Trial into a list
     * @param list Input List
     * @param inTrl Trial to insert
     * @return The list with added new trial [If input list is null, return an empty list]
     */
    private ArrayList<FittsTrial> shuffleIn(ArrayList<FittsTrial> list, FittsTrial inTrl) {
        if (list != null) {
            // If list is empty, no random index, just 0
            int rndInd = list.isEmpty() ? 0 : ThreadLocalRandom.current().nextInt(0, list.size());
            list.add(rndInd, inTrl);

            return list;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Set the current trial index
     * @param ctind Current trial index
     */
    public void setCurrTrialInd(int ctind) { currTrialInd = ctind; }

}
