import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

class AESEncrypter {
    private Cipher ecipher;
    private Cipher dcipher;

    AESEncrypter(SecretKey key) throws Exception {
        ecipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        dcipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, key);
        dcipher.init(Cipher.DECRYPT_MODE, key);
    }

    public void encrypt(String str) throws Exception {
        Base64.getEncoder().encodeToString(ecipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    public String decrypt(String str) throws Exception {
        return new String(dcipher.doFinal(Base64.getDecoder().decode(str)));
    }
} 