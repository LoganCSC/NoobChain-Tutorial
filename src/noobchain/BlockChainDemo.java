package noobchain;

import noobchain.model.Block;
import noobchain.model.BlockChain;
import noobchain.model.Transaction;
import noobchain.model.TransactionOutput;
import noobchain.model.Wallet;
import java.security.Security;

/**
 * Illustrates making transaction involving NoobCoins between block chain based Wallets.
 */
class BlockChainDemo {

    private Wallet walletA;
    private Wallet walletB;

    private BlockChain chain = new BlockChain(5);
    private Block genesis = new Block("0");

    /**
     * Test the block chain by adding blocks to the BLOCK_CHAIN ArrayList.
     */
    void run() {

        initializeWallets();

        // testing
        Block block1 = new Block(genesis.hash);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance(chain));
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f, chain), chain);
        processBlock(block1);

        Block block2 = new Block(block1.hash);
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f, chain), chain);
        processBlock(block2);

        Block block3 = new Block(block2.hash);
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20, chain), chain);
        processBlock(block3);

        assert chain.isChainValid();
    }

    // add our blocks to the blockchain ArrayList:
    private void initializeWallets() {

        // Setup Bouncy castle as a Security Provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // Create wallets:
        walletA = new Wallet();
        walletB = new Wallet();
        Wallet coinbase = new Wallet();

        // create genesis transaction, which sends 100 NoobCoin to walletA:
        chain.genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        chain.genesisTransaction.generateSignature(coinbase.privateKey); // manually sign the genesis transaction
        chain.genesisTransaction.transactionId = "0"; // manually set the transaction id

        TransactionOutput genTransaction = new TransactionOutput(chain.genesisTransaction.reciepient,
                chain.genesisTransaction.value, chain.genesisTransaction.transactionId);
        chain.genesisTransaction.outputs.add(genTransaction); // manually add the Transactions Output
        // its important to store our first transaction in the UTXOs list.
        chain.UTXOs.put(chain.genesisTransaction.outputs.get(0).id, chain.genesisTransaction.outputs.get(0));

        System.out.println("Creating and Mining Genesis block... ");

        genesis.addTransaction(chain.genesisTransaction, chain);
        chain.addBlock(genesis);
    }

    private void processBlock(Block block) {
        chain.addBlock(block);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance(chain));
        System.out.println("WalletB's balance is: " + walletB.getBalance(chain));
    }
}
