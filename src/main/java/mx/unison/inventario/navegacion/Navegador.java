package mx.unison.inventario.navegacion;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestor de navegación entre vistas de la aplicación JavaFX.
 *
 * <p>Centraliza el sistema de navegación usando un patrón de caché de vistas:
 * cada pantalla se carga una sola vez y se reutiliza en accesos subsecuentes,
 * conservando el estado de sus componentes.</p>
 *
 * <h3>Vistas registradas:</h3>
 * <ul>
 *   <li>{@code LOGIN}     — {@link mx.unison.inventario.vistas.LoginControlador}</li>
 *   <li>{@code INICIO}    — {@link mx.unison.inventario.vistas.InicioControlador}</li>
 *   <li>{@code PRODUCTOS} — {@link mx.unison.inventario.vistas.ProductosControlador}</li>
 *   <li>{@code ALMACENES} — {@link mx.unison.inventario.vistas.AlmacenesControlador}</li>
 * </ul>
 *
 * @author Sistema de Inventario v2 — UNISON
 * @version 2.0
 */
public class Navegador {

    private static final Logger LOG = Logger.getLogger(Navegador.class.getName());

    /** Mapa de rutas de vistas por nombre. */
    private static final Map<String, String> RUTAS = new HashMap<>();

    static {
        RUTAS.put("LOGIN",     "/mx/unison/inventario/vistas/login.fxml");
        RUTAS.put("INICIO",    "/mx/unison/inventario/vistas/inicio.fxml");
        RUTAS.put("PRODUCTOS", "/mx/unison/inventario/vistas/productos.fxml");
        RUTAS.put("ALMACENES", "/mx/unison/inventario/vistas/almacenes.fxml");
    }

    /** Caché de vistas ya cargadas (nombre → raíz del árbol de nodos). */
    private final Map<String, Parent> cache = new HashMap<>();

    /** Escena principal de la aplicación. */
    private final Scene escena;

    /** Stage principal. */
    private final Stage stage;

    /**
     * Construye el navegador con la escena y stage de la aplicación.
     *
     * @param stage  ventana principal de JavaFX
     * @param escena escena donde se intercambian las raíces de las vistas
     */
    public Navegador(Stage stage, Scene escena) {
        this.stage  = stage;
        this.escena = escena;
    }

    /**
     * Navega a la vista identificada por {@code nombre}.
     *
     * <p>Si la vista ya fue cargada, se reutiliza desde la caché.
     * Si no, se carga el FXML correspondiente y se registra en la caché.</p>
     *
     * @param nombre nombre de la vista (p.ej. {@code "LOGIN"}, {@code "INICIO"})
     * @throws IllegalArgumentException si el nombre no corresponde a ninguna vista registrada
     */
    public void navegar(String nombre) {
        if (!RUTAS.containsKey(nombre)) {
            throw new IllegalArgumentException("Vista no registrada: " + nombre);
        }
        try {
            if (!cache.containsKey(nombre)) {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(RUTAS.get(nombre)));
                loader.setControllerFactory(cls -> crearControlador(cls, nombre));
                Parent raiz = loader.load();
                cache.put(nombre, raiz);
            }
            escena.setRoot(cache.get(nombre));
            stage.sizeToScene();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error al cargar la vista: " + nombre, e);
            throw new RuntimeException("No se pudo cargar la vista: " + nombre, e);
        }
    }

    /**
     * Invalida la caché de la vista indicada, forzando su recarga en
     * la próxima navegación. Útil cuando los datos han cambiado.
     *
     * @param nombre nombre de la vista a invalidar
     */
    public void invalidarCache(String nombre) {
        cache.remove(nombre);
    }

    /**
     * Invalida la caché de todas las vistas cargadas.
     */
    public void invalidarTodaLaCache() {
        cache.clear();
    }

    /**
     * Crea un controlador con inyección del {@link Navegador}.
     * Las clases de controlador que implementen {@link NecesitaNavegador}
     * recibirán esta instancia automáticamente.
     *
     * @param cls    clase del controlador
     * @param nombre nombre de la vista (para logging)
     * @return instancia del controlador configurada
     */
    private Object crearControlador(Class<?> cls, String nombre) {
        try {
            Object controlador = cls.getDeclaredConstructor().newInstance();
            if (controlador instanceof NecesitaNavegador) {
                ((NecesitaNavegador) controlador).setNavegador(this);
            }
            return controlador;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al crear controlador para: " + nombre, e);
            throw new RuntimeException(e);
        }
    }
}
