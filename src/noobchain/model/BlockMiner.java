package noobchain.model;

import noobchain.model.transaction.TransactionList;
import java.util.Date;

class BlockMiner {

    TransactionList transactions = new TransactionList();
    String previousHash;
    private String merkleRoot;
    private long timeStamp; // number of milliseconds since 1/1/1970.
    private int nonce;

    BlockMiner(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
    }

    /**
     * Increases nonce value until hash target is reached.
     * @return the new hash
     */
    String mineBlock(String oldHash, int difficulty) {
        long start = System.currentTimeMillis();
        merkleRoot = transactions.getMerkleRoot();
        String hash = oldHash;
        String target = getDifficultyString(difficulty); // Create a string with difficulty * "0"

        while (!hash.substring( 0, target.length()).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("Block Mined!!! : " + hash + " in " + duration + " millis.");
        return hash;
    }

    // Calculate new hash based on blocks contents
    String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        merkleRoot
        );
    }

    // Returns difficulty string target, to compare to hash. eg difficulty of 5 will return "00000"
    private String getDifficultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }
}
