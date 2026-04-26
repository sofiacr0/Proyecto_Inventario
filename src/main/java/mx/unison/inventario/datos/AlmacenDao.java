package mx.unison.inventario.datos;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import mx.unison.inventario.modelos.AlmacenModel;
import mx.unison.inventario.utileria.FechaUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO (Data Access Object) para la entidad {@link AlmacenModel}.
 *
 * <p>Encapsula toda la lógica de acceso a la tabla {@code almacenes},
 * gestionando automáticamente los timestamps de creación y modificación.</p>
 *
 * @author Sistema de Inventario v2 — UNISON
 * @version 2.0
 */
public class AlmacenDao {

    private static final Logger LOG = Logger.getLogger(AlmacenDao.class.getName());

    /** DAO de ORMLite subyacente. */
    private final Dao<AlmacenModel, Integer> dao;

    /**
     * Construye el DAO desde el {@code connectionSource} proporcionado.
     *
     * @param cs fuente de conexiones de ORMLite
     * @throws SQLException si no puede crear el DAO interno
     */
    public AlmacenDao(ConnectionSource cs) throws SQLException {
        this.dao = DaoManager.createDao(cs, AlmacenModel.class);
    }

    /**
     * Retorna todos los almacenes ordenados por ID ascendente.
     *
     * @return lista de almacenes; nunca {@code null}, puede estar vacía
     */
    public List<AlmacenModel> listarTodos() {
        try {
            return dao.queryBuilder().orderBy("id", true).query();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al listar almacenes", e);
            return List.of();
        }
    }

    /**
     * Busca un almacén por su ID.
     *
     * @param id identificador del almacén
     * @return {@link Optional} con el almacén, o vacío si no existe
     */
    public Optional<AlmacenModel> buscarPorId(int id) {
        try {
            return Optional.ofNullable(dao.queryForId(id));
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Error al buscar almacén id=" + id, e);
            return Optional.empty();
        }
    }

    /**
     * Inserta un nuevo almacén asignando automáticamente el timestamp
     * de creación y el nombre del usuario.
     *
     * @param almacen almacén a insertar; {@code nombre} no debe ser vacío
     * @param usuario nombre del usuario que realiza la operación
     * @return el {@link AlmacenModel} persistido con su ID generado
     * @throws SQLException si ocurre un error de persistencia
     */
    public AlmacenModel insertar(AlmacenModel almacen, String usuario) throws SQLException {
        almacen.setFechaHoraCreacion(FechaUtil.ahora());
        almacen.setUltimoUsuario(usuario);
        dao.create(almacen);
        return almacen;
    }

    /**
     * Actualiza un almacén existente asignando automáticamente el
     * timestamp de modificación y el nombre del usuario.
     *
     * @param almacen almacén con los nuevos valores; {@link AlmacenModel#getId()}
     *                debe corresponder a un registro existente
     * @param usuario nombre del usuario que realiza la operación
     * @throws SQLException si ocurre un error de persistencia
     */
    public void actualizar(AlmacenModel almacen, String usuario) throws SQLException {
        almacen.setFechaHoraUltimaMod(FechaUtil.ahora());
        almacen.setUltimoUsuario(usuario);
        dao.update(almacen);
    }

    /**
     * Elimina un almacén por su ID.
     *
     * <p>Si el ID no existe, la operación no hace nada sin lanzar excepción.</p>
     *
     * @param id identificador del almacén a eliminar
     * @throws SQLException si ocurre un error de persistencia
     */
    public void eliminar(int id) throws SQLException {
        dao.deleteById(id);
    }

    /**
     * Cuenta el total de almacenes en la BD.
     *
     * @return número de registros en la tabla {@code almacenes}
     */
    public long contarTodos() {
        try {
            return dao.countOf();
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Error al contar almacenes", e);
            return 0;
        }
    }
}
