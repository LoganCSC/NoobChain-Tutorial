package noobchain.model;

import noobchain.model.transaction.Transaction;
import noobchain.model.transaction.TransactionList;
import java.util.Date;

class Block {

    String hash;
    String previousHash;

    private TransactionList transactions = new TransactionList();
    private String merkleRoot;
    private long timeStamp; // number of milliseconds since 1/1/1970.
    private int nonce;

    // Block Constructor.
    Block(String previousHash ) {
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
        merkleRoot = transactions.getMerkleRoot();
        String target = getDifficultyString(difficulty); // Create a string with difficulty * "0"
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("Block Mined!!! : " + hash + " in " + duration + " millis.");
    }

    // Add transactions to this block
    boolean addTransaction(Transaction transaction, BlockChain chain) {
        // process transaction and check if valid, unless block is genesis block then ignore.
        if (transaction == null) return false;
        if (!"0".equals(previousHash)) {
            try {
                transaction.processTransaction(chain);
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
                System.out.println("Transaction failed to process, so it was discarded.");
                return false;
            }
        }

        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }

    // Returns difficulty string target, to compare to hash. eg difficulty of 5 will return "00000"
    private String getDifficultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }

    int numTransactions() {
        return transactions.size();
    }

    Transaction getTransaction(int i) {
        return transactions.get(i);
    }
}
