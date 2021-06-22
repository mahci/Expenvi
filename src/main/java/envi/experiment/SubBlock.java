package envi.experiment;

import java.util.ArrayList;
import java.util.List;

public class SubBlock {

    private String TAG = "[[Subblock]] ";
    private boolean toLog = false;

    private ArrayList<FittsTrial> trials = new ArrayList<>(); // List of trials

    // ===============================================================================

    public SubBlock() {

    }

    public SubBlock(List<FittsTuple> combinations) {
        for (FittsTuple cmb : combinations) {
            addTrial(new FittsTrial(cmb));
        }
    }

    public void addTrial(FittsTuple combination) {
        trials.add(new FittsTrial(combination));
    }

    public void addTrial(FittsTrial newTrial) {
        trials.add(newTrial);
    }

    public boolean hasNext(int trNum) {
        if (toLog) System.out.println(TAG + "Subblock: " + trNum + " / " + trials.size());
        return trials.size() - trNum > 0;
    }

    public FittsTrial getTrial(int trNum) {
        if (trNum <= trials.size()) return trials.get(trNum - 1);
        else return null;
    }

}
