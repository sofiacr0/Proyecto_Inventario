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

public class AlmacenDao {

    private static final Logger LOG = Logger.getLogger(AlmacenDao.class.getName());

    /** DAO de ORMLite subyacente. */
    private final Dao<AlmacenModel, Integer> dao;

    public AlmacenDao(ConnectionSource cs) throws SQLException {
        this.dao = DaoManager.createDao(cs, AlmacenModel.class);
    }

    public List<AlmacenModel> listarTodos() {
        try {
            return dao.queryBuilder().orderBy("id", true).query();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al listar almacenes", e);
            return List.of();
        }
    }

    public Optional<AlmacenModel> buscarPorId(int id) {
        try {
            return Optional.ofNullable(dao.queryForId(id));
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Error al buscar almacén id=" + id, e);
            return Optional.empty();
        }
    }

    public AlmacenModel insertar(AlmacenModel almacen, String usuario) throws SQLException {
        almacen.setFechaHoraCreacion(FechaUtil.ahora());
        almacen.setUltimoUsuario(usuario);
        dao.create(almacen);
        return almacen;
    }

    public void actualizar(AlmacenModel almacen, String usuario) throws SQLException {
        almacen.setFechaHoraUltimaMod(FechaUtil.ahora());
        almacen.setUltimoUsuario(usuario);
        dao.update(almacen);
    }

    public void eliminar(int id) throws SQLException {
        dao.deleteById(id);
    }

    public long contarTodos() {
        try {
            return dao.countOf();
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Error al contar almacenes", e);
            return 0;
        }
    }
}
