package mx.unison.inventario.vistas;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import mx.unison.inventario.controladores.AuthControlador;
import mx.unison.inventario.modelos.UsuarioModel;
import mx.unison.inventario.navegacion.NecesitaNavegador;
import mx.unison.inventario.navegacion.Navegador;

import java.util.Optional;

/**
 * Controlador JavaFX de la vista de inicio de sesión.
 *
 * <p>Gestiona la interacción del formulario de login: captura las
 * credenciales del usuario, delega la autenticación al
 * {@link AuthControlador} y navega al menú principal si tiene éxito.</p>
 *
 * <h3>Binding con FXML:</h3>
 * <ul>
 *   <li>{@code #campoUsuario}    — {@link TextField} para el nombre de usuario.</li>
 *   <li>{@code #campoPassword}   — {@link PasswordField} para la contraseña.</li>
 *   <li>{@code #btnLogin}        — botón que dispara la autenticación.</li>
 *   <li>{@code #labelError}      — muestra mensajes de error al usuario.</li>
 *   <li>{@code #labelBienvenida} — mensaje de bienvenida animado.</li>
 * </ul>
 *
 * @author Sistema de Inventario v2 — UNISON
 * @version 2.0
 */
public class LoginControlador implements NecesitaNavegador {

    @FXML private TextField     campoUsuario;
    @FXML private PasswordField campoPassword;
    @FXML private Button        btnLogin;
    @FXML private Label         labelError;
    @FXML private Label         labelBienvenida;

    /** Gestor de navegación inyectado por {@link Navegador}. */
    private Navegador navegador;

    /** Lógica de autenticación separada de la vista. */
    private final AuthControlador authControlador = new AuthControlador();

    /**
     * Inicialización del controlador: se ejecuta después de cargar el FXML.
     * Configura atajos de teclado y limpia mensajes de error.
     */
    @FXML
    public void initialize() {
        labelError.setVisible(false);
        // Permitir login con Enter desde cualquier campo
        campoUsuario.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) manejarLogin(); });
        campoPassword.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) manejarLogin(); });
        // Limpiar error al tipear
        campoUsuario.textProperty().addListener((o, a, b) -> labelError.setVisible(false));
        campoPassword.textProperty().addListener((o, a, b) -> labelError.setVisible(false));
        Platform.runLater(() -> campoUsuario.requestFocus());
    }

    /** {@inheritDoc} */
    @Override
    public void setNavegador(Navegador navegador) {
        this.navegador = navegador;
    }

    /**
     * Maneja el evento del botón "Iniciar sesión".
     * Valida los campos y delega la autenticación al controlador de dominio.
     */
    @FXML
    public void manejarLogin() {
        String usuario   = campoUsuario.getText().trim();
        String password  = campoPassword.getText();

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor ingresa usuario y contraseña.");
            return;
        }

        Optional<UsuarioModel> resultado = authControlador.autenticar(usuario, password);

        if (resultado.isPresent()) {
            limpiarFormulario();
            navegador.invalidarCache("INICIO");
            navegador.navegar("INICIO");
        } else {
            mostrarError("Usuario o contraseña incorrectos.");
            campoPassword.clear();
            campoPassword.requestFocus();
        }
    }

    /**
     * Muestra un mensaje de error visible bajo los campos del formulario.
     *
     * @param mensaje texto de error a mostrar
     */
    private void mostrarError(String mensaje) {
        labelError.setText(mensaje);
        labelError.setVisible(true);
    }

    /** Limpia los campos del formulario. */
    private void limpiarFormulario() {
        campoUsuario.clear();
        campoPassword.clear();
        labelError.setVisible(false);
    }
}
