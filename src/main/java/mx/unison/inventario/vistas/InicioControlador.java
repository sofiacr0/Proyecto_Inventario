package mx.unison.inventario.vistas;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import mx.unison.inventario.controladores.AuthControlador;
import mx.unison.inventario.modelos.SesionUsuario;
import mx.unison.inventario.navegacion.NecesitaNavegador;
import mx.unison.inventario.navegacion.Navegador;

/**
 * Controlador JavaFX de la vista del menú principal (pantalla de inicio).
 *
 * <p>Muestra el nombre y rol del usuario autenticado y provee botones
 * de navegación hacia los módulos de productos, almacenes y cierre de sesión.</p>
 *
 * @author Sistema de Inventario v2 — UNISON
 * @version 2.0
 */
public class InicioControlador implements NecesitaNavegador {

    @FXML private Label labelUsuario;
    @FXML private Label labelRol;
    @FXML private Label labelBienvenida;
    @FXML private Label labelConteoProductos;
    @FXML private Label labelConteoAlmacenes;

    private Navegador navegador;
    private final AuthControlador authControlador = new AuthControlador();

    /** Inicializa la vista con los datos del usuario de la sesión activa. */
    @FXML
    public void initialize() {
        SesionUsuario sesion = SesionUsuario.getInstancia();
        labelUsuario.setText("Usuario: " + sesion.getNombreUsuario());
        labelRol.setText("Rol: " + sesion.getRolUsuario());
    }

    @Override
    public void setNavegador(Navegador navegador) {
        this.navegador = navegador;
    }

    /** Navega al módulo de inicio. */
    @FXML
    public void irInicio() {
        navegador.invalidarCache("INICIO");
        navegador.navegar("INICIO");
    }

    /** Navega al módulo de gestión de productos. */
    @FXML
    public void irProductos() {
        navegador.invalidarCache("PRODUCTOS");
        navegador.navegar("PRODUCTOS");
    }

    /** Navega al módulo de gestión de almacenes. */
    @FXML
    public void irAlmacenes() {
        navegador.invalidarCache("ALMACENES");
        navegador.navegar("ALMACENES");
    }

    /** Cierra la sesión y regresa al login. */
    @FXML
    public void cerrarSesion() {
        authControlador.cerrarSesion();
        navegador.invalidarTodaLaCache();
        navegador.navegar("LOGIN");
    }
}
