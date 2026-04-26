package mx.unison.inventario.utileria;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FechaUtil {

    /** Formato ISO-8601 estándar sin zona horaria. */
    public static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /** Constructor privado — clase de utilidad sin estado. */
    private FechaUtil() {}

    /**
     * Retorna la fecha y hora actuales formateadas en ISO-8601.
     *
     * @return cadena con formato {@code yyyy-MM-dd'T'HH:mm:ss}
     */
    public static String ahora() {
        return LocalDateTime.now().format(ISO);
    }

    /**
     * Formatea un {@link LocalDateTime} en ISO-8601.
     *
     * @param dt fecha y hora a formatear; no debe ser {@code null}
     * @return cadena con formato ISO-8601
     * @throws NullPointerException si {@code dt} es {@code null}
     */
    public static String formatear(LocalDateTime dt) {
        return dt.format(ISO);
    }
}
