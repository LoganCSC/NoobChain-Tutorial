package noobchain.model;

import noobchain.model.transaction.Transaction;
import noobchain.model.transaction.TransactionOutput;
import java.security.Security;

/**
 * CoinBase supplies the source of NoobCoins. It hides the inner workings of the block chain.
 * Protect it well. Ordinarily new NoobCoins should be mined,
 * but for convenience, this can provide arbitrary numbers of them.
 */
public class CoinBase {

    // Transactions are very slow if difficulty is 6 or greater. 5 is reasonable.
    private BlockChain chain = new BlockChain(5, 0.1f);

    public CoinBase() {
        // Setup Bouncy castle as a Security Provider. Cannot create Wallets without security
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public Wallet createWallet() {
        return new Wallet(chain);
    }

    /**
     * Create genesis transaction, which sends some NoobCoin to specified wallet.
     * Ordinarily this should be mined. Money isn't free!
     * @param numCoins number of initial coins to give specified wallet
     * @param wallet recipient wallet
     */
    public void createInitialTransaction(float numCoins, Wallet wallet) {

        Block genesis = new Block("0");

        Transaction genesisTransaction = createGenisisTransaction(numCoins, wallet);

        // its important to store our first transaction in the UTXOs list.
        chain.addUnspentTransactionOutput(genesisTransaction.outputs.get(0));

        System.out.println("Creating and Mining Genesis block... ");
        genesis.addTransaction(genesisTransaction, chain);

        chain.addBlock(genesis);
    }

    public void transfer(Wallet fromWallet, Wallet toWallet, float coins) {
        Block block = new Block(chain.getLastBlock().hash);
        block.addTransaction(fromWallet.sendFunds(toWallet.getPublicKey(), coins), chain);
        chain.addBlock(block);
        assert(chain.isChainValid());
    }

    private Transaction createGenisisTransaction(float numCoins, Wallet wallet) {
        Wallet coinBase = new Wallet(chain);
        Transaction genesisTransaction =
                new Transaction(coinBase.getPublicKey(), wallet.getPublicKey(), numCoins, null);
        // manually sign the genesis transaction with secret private key (keep secret!)
        genesisTransaction.generateSignature(coinBase.getPrivateKey());
        genesisTransaction.transactionId = "0"; // manually set the transaction id

        TransactionOutput genTransaction = new TransactionOutput(genesisTransaction.recipient,
                genesisTransaction.value, genesisTransaction.transactionId);
        // manually add the Transactions Output
        genesisTransaction.outputs.add(genTransaction);
        return genesisTransaction;
    }
}
