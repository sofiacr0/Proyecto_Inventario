package mx.unison.inventario.utileria;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidades criptográficas del sistema de inventario.
 *
 * <p>Provee el cálculo de hash <strong>MD5</strong> para la verificación
 * de contraseñas almacenadas en la base de datos.</p>
 *
 * <h3>Nota de seguridad:</h3>
 * <p>MD5 no es apto para producción en sistemas con datos sensibles.
 * Esta implementación es adecuada para el contexto académico del proyecto.
 * Para producción real se recomienda {@code BCrypt} o {@code Argon2}.</p>
 *
 * @author Sistema de Inventario v2 — UNISON
 * @version 2.0
 */
public final class CryptoUtil {

    /** Constructor privado — clase de utilidad sin estado. */
    private CryptoUtil() {}

    /**
     * Calcula el hash MD5 de la cadena de entrada codificada en UTF-8.
     *
     * <p>La salida siempre tiene exactamente 32 caracteres hexadecimales
     * en minúsculas y es determinista para la misma entrada.</p>
     *
     * @param input texto a hashear; no debe ser {@code null}
     * @return cadena hexadecimal MD5 de 32 caracteres en minúsculas
     * @throws IllegalArgumentException si {@code input} es {@code null}
     * @throws RuntimeException si el algoritmo MD5 no está disponible en la JVM
     */
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

    /**
     * Verifica si una contraseña en texto plano coincide con un hash MD5 almacenado.
     *
     * @param plainPassword contraseña en texto plano
     * @param storedHash    hash MD5 almacenado en la BD
     * @return {@code true} si el hash de {@code plainPassword} coincide con
     *         {@code storedHash}; {@code false} en caso contrario
     */
    public static boolean verificar(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) return false;
        return md5(plainPassword).equals(storedHash);
    }
}
