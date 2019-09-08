package noobchain.model.transaction;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/** A signature for private keys, and verifies signatures using public key */
class EcdsaSignature {

    private byte[] signature;

    // Applies ECDSA Signature
    EcdsaSignature(PrivateKey privateKey, String input) {
        Signature dsa;
        signature = new byte[0];
        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            signature = dsa.sign();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to sign private key with " + input, e);
        }
    }

    byte[] getSignature(PrivateKey privateKey, String input) {
        return signature;
    }

    // Verifies a String signature
    boolean verifySignature(PublicKey publicKey, String data) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        } catch(Exception e) {
            throw new IllegalStateException("Error while trying to verify pubicKey using " + data, e);
        }
    }
}
