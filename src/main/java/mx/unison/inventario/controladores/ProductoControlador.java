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

    public ProductoControlador(ProductoDao productoDao) {
        this.productoDao = productoDao;
    }

    public List<ProductoModel> listarProductos() {
        return productoDao.listarTodos();
    }

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

    public void eliminar(int id) {
        try {
            productoDao.eliminar(id);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al eliminar producto id=" + id, e);
            throw new RuntimeException("No se pudo eliminar el producto", e);
        }
    }

    public List<ProductoModel> buscar(String texto) {
        if (texto == null || texto.isBlank()) return listarProductos();
        return productoDao.buscarPorNombre(texto.trim());
    }

    public Optional<ProductoModel> buscarPorId(int id) {
        return productoDao.buscarPorId(id);
    }

    private void validar(String nombre, int cantidad, double precio) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        if (cantidad < 0)
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        if (precio < 0.0)
            throw new IllegalArgumentException("El precio no puede ser negativo.");
    }
}
