package noobchain.model;

import noobchain.model.transaction.Transaction;
import noobchain.model.transaction.TransactionInput;
import noobchain.model.transaction.TransactionOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BlockChain {

    private ArrayList<Block> blockChain = new ArrayList<>();
    private Map<String, TransactionOutput> UTXOs = new HashMap<>();
    private float minimumTransaction;
    private int difficulty;

    public BlockChain() {
        this.difficulty = 5;
        this.minimumTransaction = 0.1f;
    }

    BlockChain(int difficulty, float minimumTransaction) {
        this.difficulty = difficulty;
        this.minimumTransaction = minimumTransaction;
    }

    Boolean isChainValid() {
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
        return get(0).getTransaction(0);
    }

    /** @return the serialized blockChain */
    public String getAsJson() {
        return StringUtil.getJson(blockChain);
    }

    int getDifficulty() {
        return difficulty;
    }

    public float getMinimumTransaction() {
        return minimumTransaction;
    }

    void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockChain.add(newBlock);
    }

    Block getLastBlock() {
        return blockChain.isEmpty() ? null : blockChain.get(blockChain.size() - 1);
    }

    public void addUnspentTransactionOutput(TransactionOutput xo) {
        UTXOs.put(xo.id, xo);
    }

    Set<Map.Entry<String, TransactionOutput>> getUnspentTransactionOutputs() {
        return UTXOs.entrySet();
    }

    public TransactionOutput getUnspentTransactionOutput(TransactionInput input) {
        return UTXOs.get(input.transactionOutputId);
    }

    public TransactionOutput removeUnspentTransactionOutput(String id) {
        return UTXOs.remove(id);
    }
}
