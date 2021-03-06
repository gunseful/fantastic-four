package app.controller.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Encrypt {

    private static SecretKeySpec setKey(String myKey) {
        MessageDigest sha;
        try {
            byte[] key = myKey.getBytes(UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            return new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String encrypt(String stringToEncrypt, String secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, setKey(secret));
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(stringToEncrypt
                            .getBytes(UTF_8)));
        } catch (Exception ignored) {
            return stringToEncrypt;
        }
    }
}