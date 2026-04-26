package mx.unison.inventario.modelos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "productos")
public class ProductoModel {

    /** Identificador único autoincremental. */
    @DatabaseField(generatedId = true)
    private int id;

    /** Nombre del producto; obligatorio. */
    @DatabaseField(columnName = "nombre", canBeNull = false)
    private String nombre;

    /** Descripción detallada del producto; opcional. */
    @DatabaseField(columnName = "descripcion")
    private String descripcion;

    /** Cantidad disponible en inventario. */
    @DatabaseField(columnName = "cantidad", canBeNull = false)
    private int cantidad;

    /** Precio unitario del producto. */
    @DatabaseField(columnName = "precio")
    private double precio;

    /**
     * Referencia al almacén donde se encuentra el producto.
     * ORMLite persiste solo el ID ({@code almacen_id}) con {@code foreignAutoRefresh = false}
     * para evitar carga lazy no deseada.
     */
    @DatabaseField(columnName = "almacen_id", foreign = true, foreignAutoRefresh = true,
                   columnDefinition = "INTEGER")
    private AlmacenModel almacen;

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
    public ProductoModel() {}

    /**
     * Construye un producto con los campos obligatorios.
     *
     * @param nombre    nombre del producto
     * @param cantidad  cantidad en inventario
     * @param precio    precio unitario
     */
    public ProductoModel(String nombre, int cantidad, double precio) {
        this.nombre   = nombre;
        this.cantidad = cantidad;
        this.precio   = precio;
    }

    // ── Getters y Setters ──────────────────────────────────────────

    /** @return identificador único */
    public int getId() { return id; }

    /** @return nombre del producto */
    public String getNombre() { return nombre; }

    /** @param nombre nombre del producto */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** @return descripción del producto */
    public String getDescripcion() { return descripcion; }

    /** @param descripcion descripción del producto */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /** @return cantidad disponible */
    public int getCantidad() { return cantidad; }

    /** @param cantidad cantidad disponible */
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    /** @return precio unitario */
    public double getPrecio() { return precio; }

    /** @param precio precio unitario */
    public void setPrecio(double precio) { this.precio = precio; }

    /**
     * Retorna el {@link AlmacenModel} asociado al producto.
     * Puede ser {@code null} si el producto no tiene almacén asignado.
     *
     * @return almacén asociado, o {@code null}
     */
    public AlmacenModel getAlmacen() { return almacen; }

    /** @param almacen almacén al que pertenece el producto */
    public void setAlmacen(AlmacenModel almacen) { this.almacen = almacen; }

    /**
     * Obtiene el nombre del almacén asociado de forma segura.
     *
     * @return nombre del almacén, o {@code "—"} si no tiene almacén asignado
     */
    public String getNombreAlmacen() {
        return (almacen != null) ? almacen.getNombre() : "—";
    }

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
        return "ProductoModel{id=" + id + ", nombre='" + nombre
               + "', cantidad=" + cantidad + ", precio=" + precio + "}";
    }
}
