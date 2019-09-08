package noobchain.model;

import noobchain.model.transaction.Transaction;
import noobchain.model.transaction.TransactionInput;
import noobchain.model.transaction.TransactionOutput;
import java.util.HashMap;
import java.util.Map;

class BlockChainValidator {

    // Unspent Transaction Outputs.
    private Map<String, TransactionOutput> tempUTXOs;

    boolean isValid(BlockChain chain) {

        String hashTarget = new String(new char[chain.getDifficulty()]).replace('\0', '0');
        // a temporary working list of unspent transactions at a given block state.
        tempUTXOs = new HashMap<>();
        tempUTXOs.put(chain.getGenesisTransaction().outputs.get(0).id, chain.getGenesisTransaction().outputs.get(0));

        // loop through block chain to check hashes:
        for (int i = 1; i < chain.size(); i++) {
            if (!isValidBlock(chain, hashTarget, i))
                return false;
        }
        System.out.println("Block chain is valid");
        return true;
    }

    private boolean isValidBlock(BlockChain chain, String hashTarget, int i) {
        Block currentBlock = chain.get(i);
        Block previousBlock = chain.get(i-1);

        // compare registered hash and calculated hash:
        if (!currentBlock.isValidateHash()){
            System.out.println("#Current Hashes not equal");
            return false;
        }
        // compare previous hash and registered previous hash
        if (!previousBlock.hash.equals(currentBlock.getPreviousHash()) ) {
            System.out.println("#Previous Hashes not equal");
            return false;
        }
        // check if hash is solved
        if (!currentBlock.hash.substring( 0, chain.getDifficulty()).equals(hashTarget)) {
            System.out.println("#This block hasn't been mined");
            return false;
        }

        return hasValidTransactions(currentBlock);
    }

    private boolean hasValidTransactions(Block currentBlock) {
        // loop through the blockchain's transactions and verify each one:
        for (int t = 0; t < currentBlock.numTransactions(); t++) {
            Transaction currentTransaction = currentBlock.getTransaction(t);
            if (!isValidTransaction(currentTransaction)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidTransaction(Transaction transaction) {

        String id = transaction.transactionId;
        if (!transaction.verifySignature()) {
            System.out.println("#Signature on Transaction(" + id + ") is Invalid");
            return false;
        }
        if (transaction.getInputsValue() != transaction.getOutputsValue()) {
            System.out.println("#Inputs are note equal to outputs on Transaction(" + id + ")");
            return false;
        }

        for (TransactionInput input: transaction.inputs) {
            TransactionOutput tempOutput = tempUTXOs.get(input.transactionOutputId);

            if (tempOutput == null) {
                System.out.println("#Referenced input on Transaction(" + id + ") is Missing");
                return false;
            }

            if (input.UTXO.getValue() != tempOutput.getValue()) {
                System.out.println("#Referenced input Transaction(" + id + ") value is Invalid");
                return false;
            }

            tempUTXOs.remove(input.transactionOutputId);
        }

        for(TransactionOutput output: transaction.outputs) {
            tempUTXOs.put(output.id, output);
        }

        if (transaction.outputs.get(0).getRecipient() != transaction.recipient) {
            System.out.println("#Transaction(" + id + ") output recipient is not who it should be");
            return false;
        }
        if (transaction.outputs.get(1).getRecipient() != transaction.sender) {
            System.out.println("#Transaction(" + id + ") output 'change' is not sender.");
            return false;
        }
        return true;
    }
}
