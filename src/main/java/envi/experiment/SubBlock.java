package envi.experiment;

import envi.tools.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SubBlock {

    private String TAG = "[[Subblock]] ";
    private boolean toLog = false;

    private ArrayList<FittsTrial> trials = new ArrayList<>(); // List of trials

    // ===============================================================================

    public SubBlock() {

    }

    /**
     * Setup the subblock with a list of combinations
     * @param combinations List of FittsTuple
     */
    public SubBlock(List<FittsTuple> combinations) {
        for (FittsTuple cmb : combinations) {
            addTrial(new FittsTrial(cmb));
        }
    }

    /**
     * Add a trial to the list
     * @param combination FittsTuple
     */
    public void addTrial(FittsTuple combination) {
        trials.add(new FittsTrial(combination));
    }

    /**
     * Add a trial directly
     * @param newTrial FittsTrial
     */
    public void addTrial(FittsTrial newTrial) {
        trials.add(newTrial);
    }


    /**
     * Put back the trial (randomly) into the remaining trials
     * @param trNum int - trial number (index + 1)
     */
    public void reshuffleTrial(int trNum) {
        int index = trNum - 1;
        if (trNum > 0 && trNum < trials.size()) {
            FittsTrial trial = trials.get(index);
            int newInd = Utils.randInt(trNum + 1, trials.size() + 1);
            trials.add(newInd, trial);
        } else if (trNum == trials.size()) {
            FittsTrial trial = trials.get(index);
            trials.add(trNum, trial);
        }
    }

    /**
     * Is there more trials after this trial?
     * @param trNum int - trial number
     * @return boolean
     */
    public boolean hasNext(int trNum) {
        if (toLog) System.out.println(TAG + "Subblock: " + trNum + " / " + trials.size());
        return trials.size() - trNum > 0;
    }

    /**
     * Get the trial based on the number (index + 1)
     * @param trNum int - trial number
     * @return FittsTrial
     */
    public FittsTrial getTrial(int trNum) {
        if (trNum <= trials.size()) return trials.get(trNum - 1);
        else return null;
    }

}
