package noobchain;
import java.util.ArrayList;

class NoobChain {

    private ArrayList<Block> BLOCK_CHAIN = new ArrayList<>();
    private int difficulty;

    NoobChain(int difficulty) {
        this.difficulty = difficulty;
    }

    Boolean isChainValid() {
        NoobChainValidator validator = new NoobChainValidator();
        return validator.isValid(this);

    }

    int size() {
        return BLOCK_CHAIN.size();
    }

    Block get(int index) {
        return BLOCK_CHAIN.get(index);
    }

    String getAsJson() {
        return StringUtil.getJson(BLOCK_CHAIN);
    }

    int getDifficulty() {
        return difficulty;
    }

    void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        BLOCK_CHAIN.add(newBlock);
    }
}
