package noobchain.model.transaction;

import noobchain.model.BlockChain;
import noobchain.model.StringUtil;

public class TransactionProcessor {

    void process(Transaction txn, BlockChain chain) {
        if (!txn.verifySignature()) {
            throw new IllegalStateException(
                    "#Transaction Signature failed to verify for " + StringUtil.getJson(this));
        }

        // Gathers transaction inputs (Making sure they are unspent):
        for (TransactionInput input : txn.inputs) {
            input.UTXO = chain.getUnspentTransactionOutput(input);
        }

        // Checks if transaction is valid:
        if (txn.getInputsValue() < chain.getMinimumTransaction()) {
            throw new IllegalStateException(
                    "Transaction Inputs too small: " + txn.getInputsValue() + "\n" +
                            "Please enter the amount greater than " + chain.getMinimumTransaction());
        }

        // Generate transaction outputs:
        // get value of inputs then the left over change:
        float leftOver = txn.getInputsValue() - txn.value;
        txn.transactionId = txn.calulateHash();
        // send value to recipient
        txn.outputs.add(new TransactionOutput( txn.recipient, txn.value, txn.transactionId));
        // send the left over 'change' back to sender
        txn.outputs.add(new TransactionOutput( txn.sender, leftOver, txn.transactionId));

        // Add outputs to Unspent list
        for (TransactionOutput o : txn.outputs) {
            chain.addUnspentTransactionOutput(o);
        }

        // Remove transaction inputs from UTXO lists as spent:
        for (TransactionInput i : txn.inputs) {
            if (i.UTXO == null) continue; // if Transaction can't be found skip it
            chain.removeUnspentTransactionOutput(i.UTXO.id);
        }
    }
}
