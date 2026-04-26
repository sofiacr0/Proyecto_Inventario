package mx.unison.inventario.datos;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import mx.unison.inventario.modelos.AlmacenModel;
import mx.unison.inventario.modelos.ProductoModel;
import mx.unison.inventario.modelos.UsuarioModel;
import mx.unison.inventario.utileria.CryptoUtil;
import mx.unison.inventario.utileria.FechaUtil;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionDB {

    private static final Logger LOG = Logger.getLogger(ConexionDB.class.getName());

    /** URL de conexión JDBC para SQLite. */
    private static final String URL = "jdbc:sqlite:db/inventario_v2.db";

    /** Instancia singleton. */
    private static ConexionDB instancia;

    /** Fuente de conexiones de ORMLite. */
    private ConnectionSource connectionSource;

    /** Constructor privado — inicializa la BD. */
    private ConexionDB() {
        try {
            connectionSource = new JdbcConnectionSource(URL);
            crearTablas();
            sembrarUsuariosBase();
            LOG.info("Base de datos inicializada correctamente.");
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al inicializar la base de datos", e);
            throw new RuntimeException("No se pudo inicializar la BD", e);
        }
    }

    /**
     * Retorna la instancia única de {@code ConexionDB}, creándola si es necesario.
     *
     * @return instancia singleton de la conexión a BD
     */
    public static synchronized ConexionDB getInstancia() {
        if (instancia == null) instancia = new ConexionDB();
        return instancia;
    }

    /**
     * Retorna la {@link ConnectionSource} de ORMLite para uso en los DAOs.
     *
     * @return fuente de conexiones activa
     */
    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    /**
     * Crea todas las tablas del esquema si no existen.
     * Esta operación es idempotente y segura para llamar múltiples veces.
     *
     * @throws SQLException si ocurre un error de acceso a la BD
     */
    private void crearTablas() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, UsuarioModel.class);
        TableUtils.createTableIfNotExists(connectionSource, AlmacenModel.class);
        TableUtils.createTableIfNotExists(connectionSource, ProductoModel.class);
    }

    /**
     * Inserta los usuarios predefinidos del sistema si la tabla está vacía.
     * Los usuarios se insertan con sus contraseñas hasheadas en MD5.
     *
     * @throws SQLException si ocurre un error al consultar o insertar
     */
    private void sembrarUsuariosBase() throws SQLException {
        UsuarioDao dao = new UsuarioDao(connectionSource);
        if (dao.contarTodos() == 0) {
            dao.guardar(new UsuarioModel("ADMIN",    CryptoUtil.md5("admin23"),     "ADMIN"));
            dao.guardar(new UsuarioModel("PRODUCTOS",CryptoUtil.md5("productos19"), "PRODUCTOS"));
            dao.guardar(new UsuarioModel("ALMACENES",CryptoUtil.md5("almacenes11"), "ALMACENES"));
            LOG.info("Usuarios base creados.");
        }
    }

    /**
     * Cierra la conexión a la BD de forma limpia.
     * Debe llamarse al apagar la aplicación.
     */
    public void cerrar() {
        try {
            if (connectionSource != null) connectionSource.close();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Error al cerrar la conexión a la BD", e);
        }
    }
}
