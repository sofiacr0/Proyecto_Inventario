package mx.unison.inventario.controladores;

import mx.unison.inventario.datos.ConexionDB;
import mx.unison.inventario.datos.UsuarioDao;
import mx.unison.inventario.modelos.SesionUsuario;
import mx.unison.inventario.modelos.UsuarioModel;

import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador de dominio para la autenticación de usuarios.
 *
 * <p>Separa la lógica de autenticación de la capa de presentación.
 * La vista ({@link mx.unison.inventario.vistas.LoginControlador}) solo
 * se encarga de capturar y mostrar; este controlador decide si el
 * acceso es válido.</p>
 *
 * @author Sistema de Inventario v2 — UNISON
 * @version 2.0
 */
public class AuthControlador {

    private static final Logger LOG = Logger.getLogger(AuthControlador.class.getName());

    /** DAO de usuarios; accede a la BD mediante ORMLite. */
    private final UsuarioDao usuarioDao;

    /**
     * Construye el controlador obteniendo el DAO desde la conexión global.
     */
    public AuthControlador() {
        try {
            this.usuarioDao = new UsuarioDao(ConexionDB.getInstancia().getConnectionSource());
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al crear UsuarioDao", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor para inyección de dependencias en pruebas.
     *
     * @param usuarioDao DAO de usuarios a utilizar
     */
    public AuthControlador(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    /**
     * Autentica al usuario y, si tiene éxito, inicializa la sesión global.
     *
     * <p>Delega la verificación de credenciales a {@link UsuarioDao#autenticar}.
     * Si el resultado es positivo, registra al usuario en {@link SesionUsuario}.</p>
     *
     * @param nombre   nombre de usuario (sensible a mayúsculas)
     * @param password contraseña en texto plano
     * @return {@link Optional} con el usuario autenticado, o vacío si las
     *         credenciales son incorrectas
     */
    public Optional<UsuarioModel> autenticar(String nombre, String password) {
        Optional<UsuarioModel> resultado = usuarioDao.autenticar(nombre, password);
        resultado.ifPresent(SesionUsuario.getInstancia()::iniciarSesion);
        return resultado;
    }

    /**
     * Cierra la sesión activa limpiando el estado de {@link SesionUsuario}.
     */
    public void cerrarSesion() {
        SesionUsuario.getInstancia().cerrarSesion();
    }
}
