package mx.unison.inventario.datos;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import mx.unison.inventario.modelos.UsuarioModel;
import mx.unison.inventario.utileria.CryptoUtil;
import mx.unison.inventario.utileria.FechaUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO (Data Access Object) para la entidad {@link UsuarioModel}.
 *
 * <p>Encapsula toda la lógica de acceso a la tabla {@code usuarios},
 * delegando las operaciones CRUD a ORMLite y exponiendo una API
 * de alto nivel orientada al dominio.</p>
 *
 * @author Sistema de Inventario v2 — UNISON
 * @version 2.0
 */
public class UsuarioDao {

    private static final Logger LOG = Logger.getLogger(UsuarioDao.class.getName());

    /** DAO de ORMLite subyacente. */
    private final Dao<UsuarioModel, Integer> dao;

    /**
     * Construye el DAO obteniendo el {@link Dao} de ORMLite desde el
     * {@code connectionSource} proporcionado.
     *
     * @param cs fuente de conexiones de ORMLite
     * @throws SQLException si no puede crear el DAO interno
     */
    public UsuarioDao(ConnectionSource cs) throws SQLException {
        this.dao = DaoManager.createDao(cs, UsuarioModel.class);
    }

    /**
     * Autentica un usuario verificando nombre y contraseña.
     *
     * <p>La contraseña se hashea con MD5 antes de compararse con la BD.
     * Si las credenciales son válidas, actualiza {@code ultimoInicioSesion}.</p>
     *
     * @param nombre        nombre de usuario (sensible a mayúsculas)
     * @param passwordPlain contraseña en texto plano
     * @return {@link Optional} con el {@link UsuarioModel} si las credenciales
     *         son válidas; {@link Optional#empty()} en caso contrario
     */
    public Optional<UsuarioModel> autenticar(String nombre, String passwordPlain) {
        if (nombre == null || passwordPlain == null) return Optional.empty();
        try {
            QueryBuilder<UsuarioModel, Integer> qb = dao.queryBuilder();
            qb.where()
              .eq("nombre", nombre)
              .and()
              .eq("password", CryptoUtil.md5(passwordPlain));
            UsuarioModel u = dao.queryForFirst(qb.prepare());
            if (u != null) {
                u.setUltimoInicioSesion(FechaUtil.ahora());
                dao.update(u);
                return Optional.of(u);
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al autenticar usuario", e);
        }
        return Optional.empty();
    }

    /**
     * Persiste un nuevo usuario en la BD.
     *
     * @param usuario usuario a guardar; su {@code passwordHash} debe estar
     *                hasheado previamente con {@link CryptoUtil#md5}
     * @throws SQLException si ocurre un error de persistencia
     */
    public void guardar(UsuarioModel usuario) throws SQLException {
        dao.create(usuario);
    }

    /**
     * Retorna todos los usuarios registrados.
     *
     * @return lista de usuarios; nunca {@code null}
     */
    public List<UsuarioModel> listarTodos() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al listar usuarios", e);
            return List.of();
        }
    }

    /**
     * Cuenta el total de usuarios en la BD.
     *
     * @return número de registros en la tabla {@code usuarios}
     */
    public long contarTodos() {
        try {
            return dao.countOf();
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Error al contar usuarios", e);
            return 0;
        }
    }

    /**
     * Busca un usuario por nombre.
     *
     * @param nombre nombre a buscar
     * @return {@link Optional} con el usuario, o vacío si no existe
     */
    public Optional<UsuarioModel> buscarPorNombre(String nombre) {
        try {
            List<UsuarioModel> lista = dao.queryForEq("nombre", nombre);
            return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Error al buscar usuario por nombre", e);
            return Optional.empty();
        }
    }
}
