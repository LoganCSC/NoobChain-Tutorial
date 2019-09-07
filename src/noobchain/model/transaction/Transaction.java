package noobchain.model.transaction;
import noobchain.model.BlockChain;
import noobchain.model.StringUtil;
import java.security.*;
import java.util.ArrayList;

public class Transaction {

    public String transactionId; // Contains a hash of transaction*
    public PublicKey sender; // Senders address/public key.
    public PublicKey recipient; // Recipients address/public key.
    public float value; // Contains the amount we wish to send to the recipient.
    private byte[] signature; // This is to prevent anybody else from spending funds in our wallet.

    public ArrayList<TransactionInput> inputs;
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

    private static int sequence = 0; // A rough count of how many transactions have been generated

    // Constructor:
    public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
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

        if (!verifySignature()) {
            throw new IllegalStateException(
                    "#Transaction Signature failed to verify for " + StringUtil.getJson(this));
        }

        // Gathers transaction inputs (Making sure they are unspent):
        for (TransactionInput input : inputs) {
            input.UTXO = chain.UTXOs.get(input.transactionOutputId);
        }

        // Checks if transaction is valid:
        if (getInputsValue() < chain.getMinimumTransaction()) {
            throw new IllegalStateException(
                    "Transaction Inputs too small: " + getInputsValue() + "\n" +
                    "Please enter the amount greater than " + chain.getMinimumTransaction());
        }

        // Generate transaction outputs:
        // get value of inputs then the left over change:
        float leftOver = getInputsValue() - value;
        transactionId = calulateHash();
        // send value to recipient
        outputs.add(new TransactionOutput( this.recipient, value, transactionId));
        // send the left over 'change' back to sender
        outputs.add(new TransactionOutput( this.sender, leftOver, transactionId));

        // Add outputs to Unspent list
        for (TransactionOutput o : outputs) {
            chain.UTXOs.put(o.id , o);
        }

        // Remove transaction inputs from UTXO lists as spent:
        for(TransactionInput i : inputs) {
            if (i.UTXO == null) continue; // if Transaction can't be found skip it
            chain.UTXOs.remove(i.UTXO.id);
        }
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
        signature = StringUtil.applyECDSASig(privateKey, getData());
    }

    public boolean verifySignature() {
        return StringUtil.verifyECDSASig(sender, getData(), signature);
    }

    private String getData() {
        return StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
    }

    public float getOutputsValue() {
        float total = 0;
        for (TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }

    private String calulateHash() {
        sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(recipient) +
                        Float.toString(value) + sequence
        );
    }
}
