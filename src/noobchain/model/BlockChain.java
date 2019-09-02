package noobchain.model;

import noobchain.model.transaction.Transaction;
import noobchain.model.transaction.TransactionOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlockChain {

    private ArrayList<Block> BLOCK_CHAIN = new ArrayList<>();
    public Map<String, TransactionOutput> UTXOs = new HashMap<>();
    private float minimumTransaction;
    private int difficulty;

    public Wallet walletA;
    public Wallet walletB;
    public Transaction genesisTransaction;

    public BlockChain() {
        this.difficulty = 5;
        this.minimumTransaction = 0.1f;
    }
    public BlockChain(int difficulty, float minimumTransaction) {
        this.difficulty = difficulty;
        this.minimumTransaction = minimumTransaction;
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

    public float getMinimumTransaction() {
        return minimumTransaction;
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        BLOCK_CHAIN.add(newBlock);
    }
}
