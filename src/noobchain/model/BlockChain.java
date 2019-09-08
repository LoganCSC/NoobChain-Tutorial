package noobchain.model;

import noobchain.model.transaction.Transaction;
import noobchain.model.transaction.TransactionInput;
import noobchain.model.transaction.TransactionOutput;
import noobchain.model.transaction.UtxoMap;

import java.util.ArrayList;
import java.util.Collection;

public class BlockChain {

    private ArrayList<Block> blockChain = new ArrayList<>();
    private UtxoMap utxoMap = new UtxoMap();
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
        utxoMap.add(xo);
    }

    Collection<TransactionOutput> getUnspentTransactionOutputs() {
        return utxoMap.getUnspentTransactionOutputs();
    }

    public TransactionOutput getUnspentTransactionOutput(TransactionInput input) {
        return utxoMap.get(input.transactionOutputId);
    }

    public TransactionOutput removeUnspentTransactionOutput(String id) {
        return utxoMap.remove(id);
    }
}
