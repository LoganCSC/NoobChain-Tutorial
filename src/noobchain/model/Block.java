package noobchain.model;

import noobchain.model.transaction.Transaction;

class Block {

    String hash;
    private BlockMiner miner;

    // Block Constructor.
    Block(String previousHash) {
        miner = new BlockMiner(previousHash);
        this.hash = miner.calculateHash();
    }

    // Increases nonce value until hash target is reached.
    void mineBlock(int difficulty) {
        hash = miner.mineBlock(hash, difficulty);
    }

    // Add transactions to this block
    boolean addTransaction(Transaction transaction, BlockChain chain) {
        // process transaction and check if valid, unless block is genesis block then ignore.
        if (transaction == null) return false;
        if (!"0".equals(miner.previousHash)) {
            try {
                transaction.processTransaction(chain);
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
                System.out.println("Transaction failed to process, so it was discarded.");
                return false;
            }
        }

        miner.transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }

    int numTransactions() {
        return miner.transactions.size();
    }

    Transaction getTransaction(int i) {
        return miner.transactions.get(i);
    }

    boolean isValidateHash() {
        return hash.equals(miner.calculateHash());
    }

    String getPreviousHash() {
        return miner.previousHash;
    }
}
