package mx.unison.inventario.modelos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDateTime;

/**
 * Entidad que representa un <strong>usuario</strong> del sistema de inventario.
 *
 * <p>Mapeada a la tabla {@code usuarios} mediante anotaciones de ORMLite.
 * Los campos están validados por el controlador antes de ser persistidos.</p>
 *
 * <h3>Roles disponibles:</h3>
 * <ul>
 *   <li>{@code ADMIN}     — acceso total al sistema.</li>
 *   <li>{@code PRODUCTOS} — solo gestión de productos.</li>
 *   <li>{@code ALMACENES} — solo gestión de almacenes.</li>
 * </ul>
 *
 * @author Sistema de Inventario v2 — UNISON
 * @version 2.0
 */
@DatabaseTable(tableName = "usuarios")
public class UsuarioModel {

    /** Identificador único autoincremental. */
    @DatabaseField(generatedId = true)
    private int id;

    /** Nombre de inicio de sesión; único y no nulo. */
    @DatabaseField(columnName = "nombre", unique = true, canBeNull = false)
    private String nombre;

    /** Hash MD5 de la contraseña; nunca se almacena en texto plano. */
    @DatabaseField(columnName = "password", canBeNull = false)
    private String passwordHash;

    /** Marca de tiempo del último inicio de sesión exitoso (ISO-8601). */
    @DatabaseField(columnName = "ultimo_inicio_sesion")
    private String ultimoInicioSesion;

    /** Rol del usuario dentro del sistema. */
    @DatabaseField(columnName = "rol", canBeNull = false)
    private String rol;

    /**
     * Constructor sin argumentos requerido por ORMLite para reflexión.
     */
    public UsuarioModel() {}

    /**
     * Construye un usuario con todos los campos obligatorios.
     *
     * @param nombre       nombre de inicio de sesión
     * @param passwordHash hash MD5 de la contraseña
     * @param rol          rol del usuario
     */
    public UsuarioModel(String nombre, String passwordHash, String rol) {
        this.nombre       = nombre;
        this.passwordHash = passwordHash;
        this.rol          = rol;
    }

    // ── Getters y Setters ──────────────────────────────────────────

    /** @return identificador único del usuario */
    public int getId() { return id; }

    /** @return nombre de inicio de sesión */
    public String getNombre() { return nombre; }

    /** @param nombre nombre de inicio de sesión */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** @return hash MD5 de la contraseña */
    public String getPasswordHash() { return passwordHash; }

    /** @param passwordHash hash MD5 de la contraseña */
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    /** @return último inicio de sesión en formato ISO-8601, o {@code null} */
    public String getUltimoInicioSesion() { return ultimoInicioSesion; }

    /** @param ultimoInicioSesion timestamp ISO-8601 del último acceso */
    public void setUltimoInicioSesion(String ultimoInicioSesion) {
        this.ultimoInicioSesion = ultimoInicioSesion;
    }

    /** @return rol del usuario */
    public String getRol() { return rol; }

    /** @param rol rol del usuario */
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() {
        return "UsuarioModel{id=" + id + ", nombre='" + nombre + "', rol='" + rol + "'}";
    }
}
