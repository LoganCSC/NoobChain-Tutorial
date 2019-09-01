package noobchain.model;

import java.util.Date;

public class Block {

    public String hash;
    String previousHash;
    private String data; // our data will be a simple message.
    private long timeStamp; // as number of milliseconds since 1/1/1970.
    private long nonce = 0;

    //Block Constructor.
    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    // Calculate new hash based on blocks contents
    String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                Long.toString(timeStamp) +
                Long.toString(nonce) +
                data);
    }

    // Increases nonce value until hash target is reached.
    void mineBlock(int difficulty) {
        String target = StringUtil.getDifficultyString(difficulty); // Create a string with difficulty * "0"

        // This keeps iterating until the hash happens to be a string of 0's of length difficulty.
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

}
