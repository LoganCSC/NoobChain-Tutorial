package noobchain.model.transaction;

import noobchain.model.BlockChain;
import noobchain.model.StringUtil;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Transaction {

    public String transactionId; // Contains a hash of transaction*
    public PublicKey sender; // Senders address/public key.
    public PublicKey recipient; // Recipients address/public key.
    public float value; // Contains the amount we wish to send to the recipient.
    private EcdsaSignature signature; // This is to prevent anybody else from spending funds in our wallet.

    public List<TransactionInput> inputs;
    public List<TransactionOutput> outputs = new ArrayList<>();

    private static int sequence = 0; // A rough count of how many transactions have been generated

    // Constructor:
    public Transaction(PublicKey from, PublicKey to, float value,  List<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    /**
     * Process this transaction on the specified block chain.
     * @throws IllegalStateException if unable to process
     */
    public void processTransaction(BlockChain chain) {
        new TransactionProcessor().process(this, chain);
    }

    public float getInputsValue() {
        float total = 0;
        for(TransactionInput i : inputs) {
            // if Transaction can't be found skip it, This behavior may not be optimal.
            if (i.UTXO == null) continue;
            total += i.UTXO.value;
        }
        return total;
    }

    public void generateSignature(PrivateKey privateKey) {
        signature = new EcdsaSignature(privateKey, getData());
    }

    public boolean verifySignature() {
        return signature.verifySignature(sender, getData());
    }

    private String getData() {
        return getStringFromKey(sender) + getStringFromKey(recipient) + Float.toString(value);
    }

    public float getOutputsValue() {
        float total = 0;
        for (TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }

    String calulateHash() {
        sequence++; // increase the sequence to avoid 2 identical transactions having the same hash
        return StringUtil.applySha256(
                getStringFromKey(sender) +
                        getStringFromKey(recipient) +
                        Float.toString(value) + sequence
        );
    }

    static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
