package noobchain.model;

import noobchain.model.transaction.Transaction;

import java.util.ArrayList;
import java.util.Date;

public class Block {

    public String hash;
    String previousHash;
    ArrayList<Transaction> transactions = new ArrayList<>();

    private String merkleRoot;
    private long timeStamp; // number of milliseconds since 1/1/1970.
    private int nonce;

    // Block Constructor.
    public Block(String previousHash ) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash(); // Making sure we do this after we set the other values.
    }

    //Calculate new hash based on blocks contents
    String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        merkleRoot
        );
    }

    // Increases nonce value until hash target is reached.
    void mineBlock(int difficulty) {
        long start = System.currentTimeMillis();
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDifficultyString(difficulty); // Create a string with difficulty * "0"
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("Block Mined!!! : " + hash + " in " + duration + " millis.");
    }

    // Add transactions to this block
    public boolean addTransaction(Transaction transaction, BlockChain chain) {
        // process transaction and check if valid, unless block is genesis block then ignore.
        if (transaction == null) return false;
        if ((!"0".equals(previousHash))) {
            if ((!transaction.processTransaction(chain))) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }

        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }
}
