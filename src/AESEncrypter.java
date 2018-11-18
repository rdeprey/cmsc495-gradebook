import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;

public class AESEncrypter {
    Cipher ecipher;
    Cipher dcipher;

    AESEncrypter(SecretKey key) throws Exception {
        ecipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        dcipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, key);
        dcipher.init(Cipher.DECRYPT_MODE, key);
    }

    public String encrypt(String str) throws Exception {
        return Base64.getEncoder().encodeToString(ecipher.doFinal(str.getBytes("UTF-8")));
    }

    public String decrypt(String str) throws Exception {
        return new String(dcipher.doFinal(Base64.getDecoder().decode(str)));
    }
} 