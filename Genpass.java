import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.io.File;
import java.io.FileWriter; 
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Genpass {
	private static SecretKeySpec secretKey;
    private static byte[] key;
 
    public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
    
    public static void writeToFile(String userID, String password) 
    {
    	try {
    		File filename = new File("password.txt");
			FileWriter fw = new FileWriter(filename, true);
			fw.write(userID + " " + password + "\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    public static void main(String[] args) 
    {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String userID = "";
        String password = "";
        String encryptedPassword = "";
    	final String secretKey = "qwertyuioplkjhgfdsazxcvbnm";
        try {
        	System.out.print("Enter user ID : ");
			userID = br.readLine();
			System.out.print("Enter Password : ");
			password = br.readLine();
			encryptedPassword = Genpass.encrypt(password, secretKey) ;
			Genpass.writeToFile(userID, encryptedPassword);
        } catch (IOException e) {
			e.printStackTrace();
		}
    }
}