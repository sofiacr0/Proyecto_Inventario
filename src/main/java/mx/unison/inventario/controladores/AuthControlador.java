package mx.unison.inventario.controladores;

import mx.unison.inventario.datos.ConexionDB;
import mx.unison.inventario.datos.UsuarioDao;
import mx.unison.inventario.modelos.SesionUsuario;
import mx.unison.inventario.modelos.UsuarioModel;

import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public AuthControlador(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public Optional<UsuarioModel> autenticar(String nombre, String password) {
        Optional<UsuarioModel> resultado = usuarioDao.autenticar(nombre, password);
        resultado.ifPresent(SesionUsuario.getInstancia()::iniciarSesion);
        return resultado;
    }

    public void cerrarSesion() {
        SesionUsuario.getInstancia().cerrarSesion();
    }
}
