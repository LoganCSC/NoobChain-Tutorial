package noobchain;
import java.security.MessageDigest;
import com.google.gson.GsonBuilder;

class StringUtil {
    
    //Applies Sha256 to a string and returns the result. 
    static String applySha256(String input){
        
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            //Applies sha256 to our input, 
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            
            StringBuilder hexString = new StringBuilder(); // This will contain hash as hexidecimal
            for (byte aHash : hash) {
                String hex = Integer.toHexString(0xff & aHash);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    //Short hand helper to turn Object into a json string
    static String getJson(Object o) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(o);
    }
    
    //Returns difficulty string target, to compare to hash. eg difficulty of 5 will return "00000"  
    static String getDificultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }
}
