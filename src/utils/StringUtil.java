package utils;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class StringUtil {

    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String sign(PrivateKey privateKey, String input) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(input.getBytes(StandardCharsets.UTF_8));
            byte[] signedData = signature.sign();
            return Base64.getEncoder().encodeToString(signedData);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verify(PublicKey publicKey, String input, String signatureStr) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(input.getBytes(StandardCharsets.UTF_8));
            byte[] decodedSignature = Base64.getDecoder().decode(signatureStr);
            return signature.verify(decodedSignature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}