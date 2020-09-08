import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.io.FileReader;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Verifypass {
	private static SecretKeySpec secretKey;
    private static byte[] key;
 
    public static void setKey(final String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String decrypt(final String strToDecrypt, final String secret) 
    {
        try
        {
            setKey(secret);
            final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } 
        catch (final Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    public static String getEncryptedPassword(final String userID) 
    {
    	try {
    		final BufferedReader br = new BufferedReader(new FileReader("password.txt"));
    		String line = null;
			String id = "";String password = "";
			String[] data = new String[10];
			while((line = br.readLine()) != null) {
				data = line.split(" ");
				id = data[0];
				password = data[1];
				if(userID.equals(id)) {
                    br.close();
					return password;
				}
			}
			br.close();
    	} catch (final Exception e) {
			e.printStackTrace();
		}
    	return "";
    }
    
    public static void main(final String[] args) 
    {
    	final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String userID = "";
        String password = "";
        String encryptedPassword = "";
        String decryptedPassword = "";
    	final String secretKey = "qwertyuioplkjhgfdsazxcvbnm";
        try {
        	System.out.print("Enter user ID : ");
			userID = br.readLine();
			System.out.print("Enter Password : ");
			password = br.readLine();
			encryptedPassword = getEncryptedPassword(userID);
			if(encryptedPassword.length() != 0) {
				decryptedPassword = Verifypass.decrypt(encryptedPassword, secretKey);
				if(password.equals(decryptedPassword)) {
					System.out.println("Password is correct");
				}
				else {
					System.out.println("Password is incorrect");
				}
			}
			else {
				System.out.println("User ID does not exist");
			}
        } catch (final Exception e) {
			e.printStackTrace();
		}
    }
}