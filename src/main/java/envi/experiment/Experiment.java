package envi.experiment;

import envi.tools.Config;

import java.util.ArrayList;
import java.util.List;

public class Experiment {

    // List of blocks
    protected List<Block> blocks = new ArrayList<>();

    // ===============================================================================

    /**
     * Emtpy constructor
     */
    public Experiment() {

    }

    /**
     * Constructor
     * @param nBlocks Number of blocks
     * @param nSubInBlock Number of sub-blocks in each block
     */
    public Experiment(int nBlocks, int nSubInBlock) {

        // Create combinations
        List<FittsTuple> combinations = getCombinations();

        // Create the blocks
        for(int bi = 0; bi < nBlocks; bi++) {
            blocks.add(new Block(combinations, nSubInBlock));
        }
    }

    /**
     * Get the combinations from Config
     * @return List of combinations (FittsTuble)
     */
    protected List<FittsTuple> getCombinations() {
        List<FittsTuple> result = new ArrayList<>();

        for(int rad : Config._widthsMM) {
            for(int dist : Config._distancesMM) {
                result.add(new FittsTuple(rad, dist, 0));
                result.add(new FittsTuple(rad, dist, 1));
            }
        }

        return result;
    }

    /**
     * Get total number of blocks
     * @return Total number of blocks
     */
    public int getNBlocks() {
        return blocks.size();
    }

    /**
     * Get tatal number of sub-blocks
     * @return int
     */
    public int getTotalNSubBlocks() {
        int total = 0;
        for (Block bl : blocks) {
            total += bl.getNSubBlocks();
        }

        return total;
    }

    /**
     * Get a block by its number (from 1)
     * @param bNum Block number
     * @return Block
     */
    public Block getBlock(int bNum) {
        if (bNum <= blocks.size()) return blocks.get(bNum - 1);
        else return null;
    }

    /**
     * Are there more blocks from this block number?
     * @param bNum Block number
     * @return Boolean
     */
    public boolean hasNext(int bNum) {
        return blocks.size() - bNum > 0;
    }

}
