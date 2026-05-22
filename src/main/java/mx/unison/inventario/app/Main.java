package mx.unison.inventario.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mx.unison.inventario.datos.ConexionDB;
import mx.unison.inventario.navegacion.Navegador;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Punto de entrada principal del Sistema de Inventario v2.0.
 *
 * <p>Inicializa la aplicación JavaFX, crea la escena raíz y entrega
 * el control al {@link Navegador} para gestionar la navegación entre
 * vistas durante toda la vida de la aplicación.</p>
 *
 * <h3>Secuencia de arranque:</h3>
 * <ol>
 *   <li>JavaFX llama a {@link #start(Stage)}.</li>
 *   <li>Se inicializa {@link ConexionDB} (crea la BD si no existe).</li>
 *   <li>Se carga el FXML de login como escena raíz inicial.</li>
 *   <li>El {@link Navegador} gestiona todas las transiciones posteriores.</li>
 * </ol>
 *
 * @author Sistema de Inventario v2 — UNISON
 * @version 2.0
 */
public class Main extends Application {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    /** Instancia del navegador compartida por toda la aplicación. */
    private Navegador navegador;

    /**
     * Método de arranque de JavaFX.
     * Configura el stage, la escena y entrega el control al Navegador.
     *
     * @param stage ventana principal proporcionada por el runtime de JavaFX
     */
    @Override
    public void start(Stage stage) {
        try {
            // Inicializar BD al arrancar (crea esquema + usuarios base)
            ConexionDB.getInstancia();

            // Cargar FXML inicial (login) como raíz de la escena
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/mx/unison/inventario/vistas/login.fxml"));
            Parent raizLogin = loader.load();

            Scene escena = new Scene(raizLogin);

            // Crear el navegador con la escena y stage
            navegador = new Navegador(stage, escena);

            // Inyectar el navegador en el controlador de login ya cargado
            Object ctrl = loader.getController();
            if (ctrl instanceof mx.unison.inventario.navegacion.NecesitaNavegador) {
                ((mx.unison.inventario.navegacion.NecesitaNavegador) ctrl)
                        .setNavegador(navegador);
            }

            // Guardar la vista de login en la caché del navegador
            navegador.registrarRaiz("LOGIN", raizLogin);

            // Configurar el stage
            stage.setScene(escena);
            stage.setTitle("Sistema de Inventario v2.0 — UNISON");
            stage.setMinWidth(900);
            stage.setMinHeight(600);
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al iniciar la aplicación", e);
            throw new RuntimeException("No se pudo iniciar la aplicación", e);
        }
    }

    /**
     * Se ejecuta al cerrar la aplicación.
     * Cierra la conexión a la BD de forma limpia.
     */
    @Override
    public void stop() {
        ConexionDB.getInstancia().cerrar();
        LOG.info("Aplicación cerrada correctamente.");
    }

    /**
     * Método main — punto de entrada del JVM.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        launch(args);
    }
}