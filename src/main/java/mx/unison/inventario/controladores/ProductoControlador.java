package mx.unison.inventario.controladores;

import mx.unison.inventario.datos.ConexionDB;
import mx.unison.inventario.datos.ProductoDao;
import mx.unison.inventario.modelos.AlmacenModel;
import mx.unison.inventario.modelos.ProductoModel;
import mx.unison.inventario.modelos.SesionUsuario;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador de dominio para la gestión de productos.
 *
 * <p>Encapsula la lógica de negocio de productos: validaciones de campos
 * obligatorios, rangos de valores y coordinación con {@link ProductoDao}.
 * La capa de vista delega aquí todas las operaciones CRUD.</p>
 *
 * @author Sistema de Inventario v2 — UNISON
 * @version 2.0
 */
public class ProductoControlador {

    private static final Logger LOG = Logger.getLogger(ProductoControlador.class.getName());

    /** DAO de productos. */
    private final ProductoDao productoDao;

    /**
     * Construye el controlador obteniendo el DAO desde la conexión global.
     */
    public ProductoControlador() {
        try {
            this.productoDao = new ProductoDao(ConexionDB.getInstancia().getConnectionSource());
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al crear ProductoDao", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor para inyección de dependencias en pruebas.
     *
     * @param productoDao DAO de productos a utilizar
     */
    public ProductoControlador(ProductoDao productoDao) {
        this.productoDao = productoDao;
    }

    /**
     * Retorna todos los productos registrados.
     *
     * @return lista de productos; nunca {@code null}
     */
    public List<ProductoModel> listarProductos() {
        return productoDao.listarTodos();
    }

    /**
     * Crea un nuevo producto previa validación de todos los campos.
     *
     * @param nombre      nombre del producto; no puede ser vacío
     * @param descripcion descripción opcional
     * @param cantidad    cantidad ≥ 0
     * @param precio      precio ≥ 0.0
     * @param almacen     almacén asociado, o {@code null} si no aplica
     * @return el {@link ProductoModel} creado con su ID generado
     * @throws IllegalArgumentException si algún campo obligatorio es inválido
     * @throws RuntimeException         si ocurre un error de persistencia
     */
    public ProductoModel crear(String nombre, String descripcion,
                               int cantidad, double precio, AlmacenModel almacen) {
        validar(nombre, cantidad, precio);
        try {
            ProductoModel p = new ProductoModel(nombre.trim(), cantidad, precio);
            p.setDescripcion(descripcion != null ? descripcion.trim() : "");
            p.setAlmacen(almacen);
            return productoDao.insertar(p, SesionUsuario.getInstancia().getNombreUsuario());
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al crear producto", e);
            throw new RuntimeException("No se pudo crear el producto", e);
        }
    }

    /**
     * Actualiza un producto existente previa validación.
     *
     * @param producto    producto a actualizar
     * @param nombre      nuevo nombre
     * @param descripcion nueva descripción
     * @param cantidad    nueva cantidad ≥ 0
     * @param precio      nuevo precio ≥ 0.0
     * @param almacen     nuevo almacén, o {@code null} para desvincular
     * @throws IllegalArgumentException si algún campo obligatorio es inválido
     * @throws RuntimeException         si ocurre un error de persistencia
     */
    public void actualizar(ProductoModel producto, String nombre, String descripcion,
                           int cantidad, double precio, AlmacenModel almacen) {
        validar(nombre, cantidad, precio);
        producto.setNombre(nombre.trim());
        producto.setDescripcion(descripcion != null ? descripcion.trim() : "");
        producto.setCantidad(cantidad);
        producto.setPrecio(precio);
        producto.setAlmacen(almacen);
        try {
            productoDao.actualizar(producto, SesionUsuario.getInstancia().getNombreUsuario());
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al actualizar producto id=" + producto.getId(), e);
            throw new RuntimeException("No se pudo actualizar el producto", e);
        }
    }

    /**
     * Elimina el producto con el ID indicado.
     *
     * @param id identificador del producto a eliminar
     * @throws RuntimeException si ocurre un error de persistencia
     */
    public void eliminar(int id) {
        try {
            productoDao.eliminar(id);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al eliminar producto id=" + id, e);
            throw new RuntimeException("No se pudo eliminar el producto", e);
        }
    }

    /**
     * Busca productos cuyo nombre contenga el texto indicado.
     *
     * @param texto texto a buscar
     * @return lista de productos coincidentes; nunca {@code null}
     */
    public List<ProductoModel> buscar(String texto) {
        if (texto == null || texto.isBlank()) return listarProductos();
        return productoDao.buscarPorNombre(texto.trim());
    }

    /**
     * Busca un producto por ID.
     *
     * @param id identificador del producto
     * @return {@link Optional} con el producto, o vacío si no existe
     */
    public Optional<ProductoModel> buscarPorId(int id) {
        return productoDao.buscarPorId(id);
    }

    /**
     * Valida los campos obligatorios de un producto.
     *
     * @param nombre   nombre del producto
     * @param cantidad cantidad en inventario
     * @param precio   precio unitario
     * @throws IllegalArgumentException si alguna validación falla
     */
    private void validar(String nombre, int cantidad, double precio) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        if (cantidad < 0)
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        if (precio < 0.0)
            throw new IllegalArgumentException("El precio no puede ser negativo.");
    }
}
