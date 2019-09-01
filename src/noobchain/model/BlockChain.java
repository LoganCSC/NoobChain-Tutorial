package noobchain.model;
import java.util.ArrayList;

public class BlockChain {

    private ArrayList<Block> BLOCK_CHAIN = new ArrayList<>();
    private int difficulty;

    public BlockChain(int difficulty) {
        this.difficulty = difficulty;
    }

    public Boolean isChainValid() {
        BlockChainValidator validator = new BlockChainValidator();
        return validator.isValid(this);
    }

    public int size() {
        return BLOCK_CHAIN.size();
    }

    public Block get(int index) {
        return BLOCK_CHAIN.get(index);
    }

    public String getAsJson() {
        return StringUtil.getJson(BLOCK_CHAIN);
    }

    int getDifficulty() {
        return difficulty;
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        BLOCK_CHAIN.add(newBlock);
    }
}
