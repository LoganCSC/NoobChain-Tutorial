package noobchain.model.transaction;

import noobchain.model.StringUtil;
import java.security.PublicKey;

public class TransactionOutput {
    public String id;

    private PublicKey recipient; // the new owner of these coins.
    float value; // the amount of coins they now own
    private String parentTransactionId; // the id of the transaction this output was created in

    // Constructor
    public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        String key = Transaction.getStringFromKey(recipient) + Float.toString(value) + parentTransactionId;
        this.id = StringUtil.applySha256(key);
    }

    // Check if coin belongs to you
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == recipient);
    }

    public float getValue() {
        return value;
    }

    public PublicKey getRecipient() {
        return recipient;
    }
}
