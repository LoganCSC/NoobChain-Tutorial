package noobchain.model;

import noobchain.model.transaction.Transaction;
import noobchain.model.transaction.TransactionInput;
import noobchain.model.transaction.TransactionOutput;
import noobchain.model.transaction.UtxoMap;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.List;

public class Wallet {

    private BlockChain chain;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private UtxoMap utxoMap = new UtxoMap();

    Wallet(BlockChain chain) {
        this.chain = chain;
        generateKeyPair();
    }

    PublicKey getPublicKey() {
        return publicKey;
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random); // 256
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public float getBalance() {
        float total = 0;
        for (TransactionOutput utxo: chain.getUnspentTransactionOutputs()){
            if (utxo.isMine(publicKey)) { // if output belongs to me (if coins belong to me)
                utxoMap.add(utxo); // add it to our list of unspent transactions.
                total += utxo.getValue();
            }
        }
        return total;
    }

    /**
     * Means to create some initial money. A better way would be to mine it
     */
    Transaction createGenesisTransaction(float numCoins, Wallet wallet) {
        Transaction transaction =
                new Transaction(getPublicKey(), wallet.getPublicKey(), numCoins, null);
        // manually sign the genesis transaction with our secret private key
        transaction.generateSignature(privateKey);
        transaction.transactionId = "0"; // manually set the transaction id

        TransactionOutput genTransaction = new TransactionOutput(transaction.recipient,
                transaction.value, transaction.transactionId);
        // manually add the Transactions Output
        transaction.outputs.add(genTransaction);
        return transaction;
    }

    Transaction sendFunds(PublicKey recipient, float value) {
        if (getBalance() < value) {
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        List<TransactionInput> inputs = utxoMap.getTransactionInputs(value);

        Transaction newTransaction = new Transaction(publicKey, recipient , value, inputs);
        newTransaction.generateSignature(privateKey);

        for (TransactionInput input: inputs){
            utxoMap.remove(input.transactionOutputId);
        }

        return newTransaction;
    }
}
