package noobchain.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlockChain {

    private ArrayList<Block> BLOCK_CHAIN = new ArrayList<>();
    public Map<String, TransactionOutput> UTXOs = new HashMap<>();
    float minimumTransaction = 0.1f;
    private int difficulty;

    public Wallet walletA;
    public Wallet walletB;
    public Transaction genesisTransaction;

    public BlockChain(int difficulty) {
        this.difficulty = difficulty;
    }

    public Boolean isChainValid() {
        BlockChainValidator validator = new BlockChainValidator();
        return validator.isValid(this);
    }

    int size() {
        return BLOCK_CHAIN.size();
    }

    Block get(int index) {
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
