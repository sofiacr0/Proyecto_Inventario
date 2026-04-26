package mx.unison.inventario.vistas;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.unison.inventario.controladores.AlmacenControlador;
import mx.unison.inventario.modelos.AlmacenModel;
import mx.unison.inventario.navegacion.NecesitaNavegador;
import mx.unison.inventario.navegacion.Navegador;

import java.util.Optional;

public class AlmacenesControlador implements NecesitaNavegador {

    @FXML private TableView<AlmacenModel>       tablaAlmacenes;
    @FXML private TableColumn<AlmacenModel, Integer> colId;
    @FXML private TableColumn<AlmacenModel, String>  colNombre;
    @FXML private TableColumn<AlmacenModel, String>  colUbicacion;
    @FXML private TableColumn<AlmacenModel, String>  colCreado;
    @FXML private TableColumn<AlmacenModel, String>  colModificado;
    @FXML private TableColumn<AlmacenModel, String>  colUsuario;
    @FXML private TextField    campoBusqueda;
    @FXML private Label        labelConteo;

    private Navegador navegador;
    private final AlmacenControlador controlador = new AlmacenControlador();

    /** Inicializa las columnas y carga los datos al abrir la vista. */
    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colCreado.setCellValueFactory(new PropertyValueFactory<>("fechaHoraCreacion"));
        colModificado.setCellValueFactory(new PropertyValueFactory<>("fechaHoraUltimaMod"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("ultimoUsuario"));
        cargarDatos();
    }

    @Override
    public void setNavegador(Navegador navegador) { this.navegador = navegador; }

    /** Regresa al menú principal. */
    @FXML public void regresar() { navegador.navegar("INICIO"); }

    /** Abre el diálogo para agregar un nuevo almacén. */
    @FXML
    public void agregar() {
        mostrarDialogo(null).ifPresent(campos -> {
            try {
                controlador.crear(campos[0], campos[1]);
                cargarDatos();
            } catch (IllegalArgumentException e) {
                mostrarError(e.getMessage());
            }
        });
    }

    /** Abre el diálogo para editar el almacén seleccionado. */
    @FXML
    public void editar() {
        AlmacenModel sel = tablaAlmacenes.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAviso("Selecciona un almacén para editar."); return; }
        mostrarDialogo(sel).ifPresent(campos -> {
            try {
                controlador.actualizar(sel, campos[0], campos[1]);
                cargarDatos();
            } catch (IllegalArgumentException e) {
                mostrarError(e.getMessage());
            }
        });
    }

    /** Elimina el almacén seleccionado tras confirmación. */
    @FXML
    public void eliminar() {
        AlmacenModel sel = tablaAlmacenes.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAviso("Selecciona un almacén para eliminar."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "¿Eliminar el almacén \"" + sel.getNombre() + "\"?",
            ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText(null);
        confirm.showAndWait().filter(r -> r == ButtonType.YES).ifPresent(r -> {
            controlador.eliminar(sel.getId());
            cargarDatos();
        });
    }

    /** Recarga la tabla desde la BD. */
    private void cargarDatos() {
        var lista = controlador.listarAlmacenes();
        tablaAlmacenes.setItems(FXCollections.observableArrayList(lista));
        labelConteo.setText("Total: " + lista.size() + " almacen(es)");
    }

    /**
     * Muestra un diálogo de formulario para crear o editar un almacén.
     *
     * @param almacen almacén existente para edición, o {@code null} para creación
     * @return {@link Optional} con array [nombre, ubicacion] si el usuario aceptó
     */
    private Optional<String[]> mostrarDialogo(AlmacenModel almacen) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle(almacen == null ? "Nuevo Almacén" : "Editar Almacén");
        dialog.setHeaderText(null);

        TextField fNombre    = new TextField(almacen != null ? almacen.getNombre() : "");
        TextField fUbicacion = new TextField(almacen != null ? almacen.getUbicacion() : "");
        fNombre.setPromptText("Nombre del almacén");
        fUbicacion.setPromptText("Ubicación (opcional)");

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(16));
        grid.add(new Label("Nombre *:"),   0, 0); grid.add(fNombre,    1, 0);
        grid.add(new Label("Ubicación:"),  0, 1); grid.add(fUbicacion, 1, 1);
        dialog.getDialogPane().setContent(grid);

        ButtonType btnOk = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnOk, ButtonType.CANCEL);
        dialog.setResultConverter(bt -> bt == btnOk
            ? new String[]{fNombre.getText(), fUbicacion.getText()} : null);

        return dialog.showAndWait();
    }

    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }

    private void mostrarAviso(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }
}
