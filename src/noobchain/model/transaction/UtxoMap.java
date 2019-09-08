package noobchain.model.transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtxoMap {

    // UTXO = Unspent Transaction Output
    private Map<String, TransactionOutput> UTXOs = new HashMap<>();

    public void add(TransactionOutput tout) {
        UTXOs.put(tout.id, tout);
    }

    public TransactionOutput get(String id) {
        return UTXOs.get(id);
    }

    public TransactionOutput remove(String id) {
        return UTXOs.remove(id);
    }

    public Collection<TransactionOutput> getUnspentTransactionOutputs() {
        return UTXOs.values();
    }

    public List<TransactionInput> getTransactionInputs(float value) {
        ArrayList<TransactionInput> inputs = new ArrayList<>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            total += UTXO.getValue();
            inputs.add(new TransactionInput(UTXO.id));
            if (total > value) break;
        }
        return inputs;
    }
}
