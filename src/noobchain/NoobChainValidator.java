package noobchain;

public class NoobChainValidator {

    public boolean isValid(NoobChain chain) {

        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[chain.getDifficulty()]).replace('\0', '0');

        // loop through block chain to check hashes:
        for (int i = 1; i < chain.size(); i++) {
            currentBlock = chain.get(i);
            previousBlock = chain.get(i - 1);
            // compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("Current Hashes not equal");
                return false;
            }
            // compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            // check if hash is solved
            if(!currentBlock.hash.substring( 0, chain.getDifficulty()).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }

        }
        return true;
    }
}
