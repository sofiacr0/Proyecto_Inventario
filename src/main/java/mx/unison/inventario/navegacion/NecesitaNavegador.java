package mx.unison.inventario.navegacion;

/**
 * Interfaz marcadora que indica que un controlador JavaFX requiere
 * acceso al {@link Navegador} para cambiar de vista.
 *
 * <p>El {@link Navegador} inyecta automáticamente su instancia en cualquier
 * controlador que implemente esta interfaz durante la carga del FXML.</p>
 *
 * @author Sistema de Inventario v2 — UNISON
 * @version 2.0
 * @see Navegador
 */
public interface NecesitaNavegador {

    /**
     * Recibe la instancia del {@link Navegador} de la aplicación.
     *
     * @param navegador instancia del gestor de navegación
     */
    void setNavegador(Navegador navegador);
}
