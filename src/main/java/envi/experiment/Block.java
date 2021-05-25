package envi.experiment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/***
 * The class for the a block of trials
 */
public class Block {

    private String TAG = "[[Block]] ";
    private boolean toLog = true;
    //==================================================

    private ArrayList<FittsTrial> trials = new ArrayList<>(); // List of trials
    private TRIAL_TYPE trialsType;
    private int currTrialInd;

    /**
     * Constructor
     * @param trialsType Type of the trial (only Fitts for now)
     */
    public Block(TRIAL_TYPE trialsType) {
        this.trialsType = trialsType;
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
     * Set up the Fitt's trials in the list of trials
     * @param radDistDirList List of (radius, distance, direction)
     * @param dispW Width of the display area
     * @param dispH Height of the display area
     * @return Modified instance
     */
    public Block setupFittsTrials(List<List<Integer>> radDistDirList, int dispW, int dispH) {

        Collections.shuffle(radDistDirList); // Shuffle the list to get random combinations

        int nTrials = radDistDirList.size(); // Number of trials is all the permutations

        // Create trials
        for (int t = 0; t < nTrials; t ++) {
            trials.add(new FittsTrial(
                    dispW, dispH,
                    radDistDirList.get(t).get(0),
                    radDistDirList.get(t).get(1),
                    radDistDirList.get(t).get(2)));
        }

        return this;
    }

    /**
     * Output the next trial
     * @param wasSuccess Was the previous trial successful?
     * @return The next trial
     */
    public FittsTrial getNextTrial(boolean wasSuccess) {
        currTrialInd++;
        if (wasSuccess) { // Success => normal return
            if (currTrialInd == trials.size()) return null;
        } else { // Fail => do the procedure
            trialFail(currTrialInd);
        }

        return trials.get(currTrialInd); // Should always be a trial!
    }

    /**
     * Trial of @trialInd was failed => shuffle it to another future palce
     * @param trialInd Index of the trial (from 1)
     */
    public void trialFail(int trialInd) {
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


    /**
     * For testing
     */
    public void printTrialsVars() {
        System.out.println("Trials ----------------------------");
        for (int tr = 0; tr < trials.size(); tr++) {
            System.out.println("Trial #" + tr + " :");
            System.out.println("Rad/Dist/Dir -> " +
                    trials.get(tr).getTarRad() + " / " +
                    trials.get(tr).getTarDist() + " / " +
                    trials.get(tr).getTarDir());
            System.out.println("----------------------------");
        }
    }


}
