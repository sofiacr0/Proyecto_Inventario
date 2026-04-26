package mx.unison.inventario.modelos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Entidad que representa un <strong>almacén</strong> físico o lógico
 * donde se guardan los productos del inventario.
 *
 * <p>Mapeada a la tabla {@code almacenes} mediante anotaciones de ORMLite.
 * Soporta timestamping automático de creación y modificación.</p>
 *
 * @author Sistema de Inventario v2 — UNISON
 * @version 2.0
 * @see ProductoModel
 */
@DatabaseTable(tableName = "almacenes")
public class AlmacenModel {

    /** Identificador único autoincremental. */
    @DatabaseField(generatedId = true)
    private int id;

    /** Nombre del almacén; obligatorio. */
    @DatabaseField(columnName = "nombre", canBeNull = false)
    private String nombre;

    /** Ubicación geográfica o lógica del almacén; opcional. */
    @DatabaseField(columnName = "ubicacion")
    private String ubicacion;

    /** Timestamp de creación en formato ISO-8601. */
    @DatabaseField(columnName = "fecha_hora_creacion")
    private String fechaHoraCreacion;

    /** Timestamp de última modificación en formato ISO-8601; {@code null} hasta el primer update. */
    @DatabaseField(columnName = "fecha_hora_ultima_modificacion")
    private String fechaHoraUltimaMod;

    /** Nombre del usuario que realizó la última operación. */
    @DatabaseField(columnName = "ultimo_usuario_en_modificar")
    private String ultimoUsuario;

    /**
     * Constructor sin argumentos requerido por ORMLite para reflexión.
     */
    public AlmacenModel() {}

    /**
     * Construye un almacén con nombre y ubicación.
     *
     * @param nombre    nombre descriptivo del almacén
     * @param ubicacion ubicación del almacén (puede ser {@code null})
     */
    public AlmacenModel(String nombre, String ubicacion) {
        this.nombre    = nombre;
        this.ubicacion = ubicacion;
    }

    // ── Getters y Setters ──────────────────────────────────────────

    /** @return identificador único */
    public int getId() { return id; }

    /** @return nombre del almacén */
    public String getNombre() { return nombre; }

    /** @param nombre nombre del almacén */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** @return ubicación del almacén */
    public String getUbicacion() { return ubicacion; }

    /** @param ubicacion ubicación del almacén */
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    /** @return timestamp de creación en ISO-8601 */
    public String getFechaHoraCreacion() { return fechaHoraCreacion; }

    /** @param fechaHoraCreacion timestamp ISO-8601 de creación */
    public void setFechaHoraCreacion(String fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    /** @return timestamp de última modificación, o {@code null} */
    public String getFechaHoraUltimaMod() { return fechaHoraUltimaMod; }

    /** @param fechaHoraUltimaMod timestamp ISO-8601 de modificación */
    public void setFechaHoraUltimaMod(String fechaHoraUltimaMod) {
        this.fechaHoraUltimaMod = fechaHoraUltimaMod;
    }

    /** @return nombre del último usuario que modificó el registro */
    public String getUltimoUsuario() { return ultimoUsuario; }

    /** @param ultimoUsuario nombre del usuario */
    public void setUltimoUsuario(String ultimoUsuario) { this.ultimoUsuario = ultimoUsuario; }

    @Override
    public String toString() {
        return "AlmacenModel{id=" + id + ", nombre='" + nombre + "', ubicacion='" + ubicacion + "'}";
    }
}
