package noobchain;

import noobchain.model.Block;
import noobchain.model.BlockChain;
import noobchain.model.Wallet;
import noobchain.model.transaction.Transaction;
import noobchain.model.transaction.TransactionOutput;
import java.security.Security;

/**
 * CoinBase supplies the source of NoobCoins.
 * Protect it well. Ordinarily new NoobCoins should be mined,
 * but for convenience, this can provide arbitrary numbers of them.
 */
class CoinBase {

    // Transactions are very slow if difficulty is 6 or greater. 5 is reasonable.
    private BlockChain chain = new BlockChain(5, 0.1f);

    CoinBase() {
        // Setup Bouncy castle as a Security Provider. Cannot create Wallets without security
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    Wallet createWallet() {
        return new Wallet(chain);
    }

    /**
     * Create genesis transaction, which sends some NoobCoin to specified wallet.
     * Ordinarily this should be mined. Money isn't free!
     * @param numCoins number of initial coins to give specified wallet
     * @param wallet recipient wallet
     */
    void createInitialTransaction(float numCoins, Wallet wallet) {

        Block genesis = new Block("0");

        Transaction genesisTransaction = createGenisisTransaction(numCoins, wallet);

        // its important to store our first transaction in the UTXOs list.
        chain.UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        System.out.println("Creating and Mining Genesis block... ");
        genesis.addTransaction(genesisTransaction, chain);

        chain.addBlock(genesis);
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

    void transfer(Wallet fromWallet, Wallet toWallet, float coins) {
        Block block = new Block(chain.getLastBlock().hash);
        block.addTransaction(fromWallet.sendFunds(toWallet.getPublicKey(), coins), chain);
        chain.addBlock(block);
        assert(chain.isChainValid());
    }
}
