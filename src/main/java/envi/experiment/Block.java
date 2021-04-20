package envi.experiment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/***
 * The class for the a block of trials
 */
public class Block {

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

        // Get a new index for the failed trial
        int minNewInd = fi + 1;
        int maxNewInd = trials.size();
        int newInd = ThreadLocalRandom.current().nextInt(minNewInd, maxNewInd);

        // Change places
        ArrayList<FittsTrial> newList = new ArrayList<>();
        int i = 0;
        for (i = 0; i < fi; i ++) { // Copy previous trials
            newList.add(trials.get(i));
        }
        for (i = fi; i < newInd; i ++) { // Shift next trials (including newInd from old list)
            newList.add(i, trials.get(i + 1));
        }
        newList.add(newInd, trials.get(fi)); // Set the failed trial
        for (i = newInd + 1; i < trials.size(); i ++) {
            newList.add(trials.get(i));
        }

        // Replace the list
        trials = newList;
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
