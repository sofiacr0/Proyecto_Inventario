package mx.unison.inventario.modelos;

public class SesionUsuario {

    /** Instancia única del Singleton. */
    private static final SesionUsuario INSTANCIA = new SesionUsuario();

    /** Usuario actualmente autenticado; {@code null} cuando no hay sesión. */
    private UsuarioModel usuarioActual;

    /** Constructor privado — impide instanciación externa. */
    private SesionUsuario() {}

    /**
     * Retorna la instancia única del gestor de sesión.
     *
     * @return instancia singleton de {@code SesionUsuario}
     */
    public static SesionUsuario getInstancia() {
        return INSTANCIA;
    }

    /**
     * Inicia sesión con el usuario proporcionado.
     *
     * @param usuario usuario autenticado; no debe ser {@code null}
     * @throws IllegalArgumentException si {@code usuario} es {@code null}
     */
    public void iniciarSesion(UsuarioModel usuario) {
        if (usuario == null) throw new IllegalArgumentException("El usuario no puede ser null");
        this.usuarioActual = usuario;
    }

    /** Cierra la sesión actual limpiando el usuario almacenado. */
    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    /**
     * @return {@code true} si hay un usuario autenticado actualmente
     */
    public boolean isAutenticado() {
        return usuarioActual != null;
    }

    /**
     * Retorna el usuario autenticado actualmente.
     *
     * @return usuario de la sesión activa, o {@code null} si no hay sesión
     */
    public UsuarioModel getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Conveniencia: retorna el nombre del usuario activo.
     *
     * @return nombre del usuario, o {@code "SISTEMA"} si no hay sesión
     */
    public String getNombreUsuario() {
        return usuarioActual != null ? usuarioActual.getNombre() : "SISTEMA";
    }

    /**
     * Conveniencia: retorna el rol del usuario activo.
     *
     * @return rol del usuario, o cadena vacía si no hay sesión
     */
    public String getRolUsuario() {
        return usuarioActual != null ? usuarioActual.getRol() : "";
    }
}
