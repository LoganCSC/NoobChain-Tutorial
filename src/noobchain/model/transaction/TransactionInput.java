package noobchain.model.transaction;

public class TransactionInput {
    // Reference to TransactionOutput's transactionId
    public String transactionOutputId;

    // UTXO = unspent transaction outputs. Contains the Unspent transaction output
    public TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}
