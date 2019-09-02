package noobchain;

import noobchain.model.Block;
import noobchain.model.BlockChain;
import noobchain.model.transaction.Transaction;
import noobchain.model.transaction.TransactionOutput;
import noobchain.model.Wallet;
import java.security.Security;

/**
 * Illustrates making transaction involving NoobCoins between block chain based Wallets.
 * Transfers coins from one wallet to another and then back again.
 */
class TransferNoobCoinsDemo {

    private Wallet walletA;
    private Wallet walletB;

    private BlockChain chain = new BlockChain(5, 0.1f);
    private Block genesis = new Block("0");

    void run() {

        initializeWallets();

        // testing
        Block block1 = new Block(genesis.hash);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f), chain);
        processBlock(block1);

        Block block2 = new Block(block1.hash);
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f), chain);
        processBlock(block2);

        Block block3 = new Block(block2.hash);
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20), chain);
        processBlock(block3);

        assert chain.isChainValid();
    }

    // add our blocks to the blockchain ArrayList:
    private void initializeWallets() {

        // Setup Bouncy castle as a Security Provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // Create wallets:
        walletA = new Wallet(chain);
        walletB = new Wallet(chain);
        Wallet coinBase = new Wallet(chain);

        // create genesis transaction, which sends 100 NoobCoin to walletA:
        chain.genesisTransaction = new Transaction(coinBase.publicKey, walletA.publicKey, 100f, null);
        chain.genesisTransaction.generateSignature(coinBase.privateKey); // manually sign the genesis transaction
        chain.genesisTransaction.transactionId = "0"; // manually set the transaction id

        TransactionOutput genTransaction = new TransactionOutput(chain.genesisTransaction.recipient,
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
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());
    }
}
