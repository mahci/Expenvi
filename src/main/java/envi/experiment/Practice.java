package envi.experiment;

import java.util.List;

public class Practice extends Experiment {

    private final String TAG = "[[ExperimentPanel]] ";
    private final boolean toLog = true;
    // -------------------------------------------------------------------------------

    // ===============================================================================

    /**
     * Create a practice set
     * @param nBlocks Number of blocks
     * @param nSubInBlock Number of subblocks in each block
     */
    public Practice(int nBlocks, int nSubInBlock) {

        // Create combinations
        List<FittsTuple> combinations = getCombinations();

        // Create the blocks
        for(int bi = 0; bi < nBlocks; bi++) {
            blocks.add(new Block(combinations, nSubInBlock));
        }
    }

}
