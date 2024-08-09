package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import android.os.Build;
import android.util.Log;

public class Encryptor {
    private static final String ALGORITHM_TYPE = "SHA-1";
    private static final String SALT = "ds@sdf1%fd3.._~``_##****asd@@>><,??:'aas'";

    public static String encryptString(String input) {

        String result = null;
        input += SALT;

       try {
           MessageDigest md = MessageDigest.getInstance(ALGORITHM_TYPE);
           byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));

           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               result = Base64.getEncoder().encodeToString(digest);
           }
       } catch (Exception e) {
           Log.d("ENCRYPTOR", "encryptString: " + e.getMessage());
       }

        return result;
    };
}
