package mx.unison.inventario.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mx.unison.inventario.navegacion.Navegador;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        // 👇 Escena vacía al inicio
        Scene scene = new Scene(new javafx.scene.layout.StackPane());

        // 👇 Crear navegador con Stage + Scene
        Navegador navegador = new Navegador(stage, scene);

        // 👇 Configurar ventana
        stage.setTitle("Sistema de Inventario");
        stage.setScene(scene);
        stage.show();

        // 👇 Navegar a LOGIN (esto carga el FXML automáticamente)
        navegador.navegar("LOGIN");
    }

    public static void main(String[] args) {
        launch(args);
    }
}