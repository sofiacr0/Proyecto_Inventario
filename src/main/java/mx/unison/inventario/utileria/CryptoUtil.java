package mx.unison.inventario.utileria;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class CryptoUtil {

    private CryptoUtil() {}
    public static String md5(String input) {
        if (input == null) throw new IllegalArgumentException("La entrada no puede ser null");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(32);
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 no disponible en esta JVM", e);
        }
    }

    public static boolean verificar(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) return false;
        return md5(plainPassword).equals(storedHash);
    }
}
