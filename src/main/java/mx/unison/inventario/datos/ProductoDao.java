package mx.unison.inventario.datos;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import mx.unison.inventario.modelos.AlmacenModel;
import mx.unison.inventario.modelos.ProductoModel;
import mx.unison.inventario.utileria.FechaUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductoDao {

    private static final Logger LOG = Logger.getLogger(ProductoDao.class.getName());

    /** DAO de ORMLite subyacente para productos. */
    private final Dao<ProductoModel, Integer> dao;

    /**
     * Construye el DAO desde el {@code connectionSource} proporcionado.
     *
     * @param cs fuente de conexiones de ORMLite
     * @throws SQLException si no puede crear el DAO interno
     */
    public ProductoDao(ConnectionSource cs) throws SQLException {
        this.dao = DaoManager.createDao(cs, ProductoModel.class);
    }

    /**
     * Retorna todos los productos con su almacén resuelto (eager refresh).
     *
     * @return lista de productos; nunca {@code null}, puede estar vacía
     */
    public List<ProductoModel> listarTodos() {
        try {
            List<ProductoModel> lista = dao.queryBuilder().orderBy("id", true).query();
            // Forzar resolución del almacén en cada producto
            for (ProductoModel p : lista) {
                if (p.getAlmacen() != null) {
                    dao.getConnectionSource();
                }
            }
            return lista;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al listar productos", e);
            return List.of();
        }
    }

    /**
     * Busca un producto por su ID.
     *
     * @param id identificador del producto
     * @return {@link Optional} con el producto, o vacío si no existe
     */
    public Optional<ProductoModel> buscarPorId(int id) {
        try {
            return Optional.ofNullable(dao.queryForId(id));
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Error al buscar producto id=" + id, e);
            return Optional.empty();
        }
    }

    /**
     * Busca productos por almacén.
     *
     * @param almacen almacén cuyos productos se desean obtener
     * @return lista de productos del almacén; nunca {@code null}
     */
    public List<ProductoModel> buscarPorAlmacen(AlmacenModel almacen) {
        try {
            return dao.queryForEq("almacen_id", almacen.getId());
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Error al buscar productos por almacén", e);
            return List.of();
        }
    }

    /**
     * Inserta un nuevo producto asignando automáticamente el timestamp
     * de creación y el nombre del usuario.
     *
     * @param producto producto a insertar; {@code nombre} y {@code cantidad}
     *                 no deben ser nulos
     * @param usuario  nombre del usuario que realiza la operación
     * @return el {@link ProductoModel} persistido con su ID generado
     * @throws SQLException si ocurre un error de persistencia
     */
    public ProductoModel insertar(ProductoModel producto, String usuario) throws SQLException {
        producto.setFechaHoraCreacion(FechaUtil.ahora());
        producto.setUltimoUsuario(usuario);
        dao.create(producto);
        return producto;
    }

    /**
     * Actualiza un producto existente asignando automáticamente el
     * timestamp de modificación y el nombre del usuario.
     *
     * @param producto producto con los nuevos valores
     * @param usuario  nombre del usuario que realiza la operación
     * @throws SQLException si ocurre un error de persistencia
     */
    public void actualizar(ProductoModel producto, String usuario) throws SQLException {
        producto.setFechaHoraUltimaMod(FechaUtil.ahora());
        producto.setUltimoUsuario(usuario);
        dao.update(producto);
    }

    /**
     * Elimina un producto por su ID.
     *
     * <p>Si el ID no existe, la operación no hace nada sin lanzar excepción.</p>
     *
     * @param id identificador del producto a eliminar
     * @throws SQLException si ocurre un error de persistencia
     */
    public void eliminar(int id) throws SQLException {
        dao.deleteById(id);
    }

    /**
     * Cuenta el total de productos en la BD.
     *
     * @return número de registros en la tabla {@code productos}
     */
    public long contarTodos() {
        try {
            return dao.countOf();
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Error al contar productos", e);
            return 0;
        }
    }

    /**
     * Busca productos cuyo nombre contenga el texto indicado (búsqueda parcial).
     *
     * @param texto texto a buscar en el nombre del producto
     * @return lista de productos coincidentes; nunca {@code null}
     */
    public List<ProductoModel> buscarPorNombre(String texto) {
        try {
            QueryBuilder<ProductoModel, Integer> qb = dao.queryBuilder();
            qb.where().like("nombre", "%" + texto + "%");
            return qb.query();
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Error al buscar productos por nombre", e);
            return List.of();
        }
    }
}
