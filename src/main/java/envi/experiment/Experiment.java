package envi.experiment;

import envi.tools.Configs;

import java.util.ArrayList;
import java.util.List;

public class Experiment {

    // List of blocks
    protected List<Block> blocks = new ArrayList<>();

    // Number of sub-blocks in a block
    protected int nSubInBlock;

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

        // Set vars
        this.nSubInBlock = nSubInBlock;

        // Create combinations
        List<FittsTuple> combinations = getCombinations();

        // Create the blocks
        for(int bi = 0; bi < nBlocks; bi++) {
            blocks.add(new Block(combinations, nSubInBlock));
        }
    }

    /**
     * Get the combinations from Configs
     * @return List of combinations (FittsTuble)
     */
    protected List<FittsTuple> getCombinations() {
        List<FittsTuple> result = new ArrayList<>();

        for(double w : Configs._widthsMM) {
            for(double d : Configs._distancesMM) {
                result.add(new FittsTuple(w, d, 0));
                result.add(new FittsTuple(w, d, 1));
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
     * Get the number of sub-blocks in a block
     * @return Number of sub-blocks
     */
    public int getNSubInBlock() {
        return nSubInBlock;
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

    /**
     * Check whether the Experiment is finished
     * @param overallSblkNum The overall number of subblocks
     * @return boolean
     */
    public boolean isFinished(int overallSblkNum) {
        return overallSblkNum == getTotalNSubBlocks();
    }
}
