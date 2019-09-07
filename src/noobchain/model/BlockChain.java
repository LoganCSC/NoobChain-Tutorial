package noobchain.model;

import noobchain.model.transaction.Transaction;
import noobchain.model.transaction.TransactionOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlockChain {

    private ArrayList<Block> blockChain = new ArrayList<>();
    public Map<String, TransactionOutput> UTXOs = new HashMap<>();
    private float minimumTransaction;
    private int difficulty;

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
        return blockChain.size();
    }

    Block get(int index) {
        return blockChain.get(index);
    }

    Transaction getGenesisTransaction() {
        return get(0).transactions.get(0);
    }

    public String getAsJson() {
        return StringUtil.getJson(blockChain);
    }

    int getDifficulty() {
        return difficulty;
    }

    public float getMinimumTransaction() {
        return minimumTransaction;
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockChain.add(newBlock);
    }

    public Block getLastBlock() {
        return blockChain.isEmpty() ? null : blockChain.get(blockChain.size() - 1);
    }
}
