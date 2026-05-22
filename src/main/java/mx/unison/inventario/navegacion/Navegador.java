package mx.unison.inventario.navegacion;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;
/**
 * Gestor de navegación con caché de vistas FXML.
 * @author Sistema de Inventario v2 — UNISON
 * @version 2.0
 */
public class Navegador {
    private static final Logger LOG=Logger.getLogger(Navegador.class.getName());
    private static final Map<String,String> RUTAS=new HashMap<>();
    static{
        RUTAS.put("LOGIN",     "/mx/unison/inventario/vistas/login.fxml");
        RUTAS.put("INICIO",    "/mx/unison/inventario/vistas/inicio.fxml");
        RUTAS.put("PRODUCTOS", "/mx/unison/inventario/vistas/productos.fxml");
        RUTAS.put("ALMACENES", "/mx/unison/inventario/vistas/almacenes.fxml");
    }
    private final Map<String,Parent> cache=new HashMap<>();
    private final Scene escena;
    private final Stage stage;
    public Navegador(Stage stage,Scene escena){this.stage=stage;this.escena=escena;}
    public void registrarRaiz(String nombre,Parent raiz){cache.put(nombre,raiz);}
    public void navegar(String nombre){
        if(!RUTAS.containsKey(nombre)) throw new IllegalArgumentException("Vista no registrada: "+nombre);
        try{
            if(!cache.containsKey(nombre)){
                FXMLLoader loader=new FXMLLoader(getClass().getResource(RUTAS.get(nombre)));
                loader.setControllerFactory(cls->crearControlador(cls));
                cache.put(nombre,loader.load());
            }
            escena.setRoot(cache.get(nombre));
            stage.sizeToScene();
        }catch(IOException e){LOG.log(Level.SEVERE,"Error cargando vista: "+nombre,e);throw new RuntimeException(e);}
    }
    public void invalidarCache(String nombre){cache.remove(nombre);}
    public void invalidarTodaLaCache(){cache.clear();}
    private Object crearControlador(Class<?> cls){
        try{
            Object c=cls.getDeclaredConstructor().newInstance();
            if(c instanceof NecesitaNavegador)((NecesitaNavegador)c).setNavegador(this);
            return c;
        }catch(Exception e){throw new RuntimeException("Error creando: "+cls.getSimpleName(),e);}
    }
}