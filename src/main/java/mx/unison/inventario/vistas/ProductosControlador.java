package mx.unison.inventario.vistas;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import mx.unison.inventario.controladores.AlmacenControlador;
import mx.unison.inventario.controladores.ProductoControlador;
import mx.unison.inventario.modelos.AlmacenModel;
import mx.unison.inventario.modelos.ProductoModel;
import mx.unison.inventario.navegacion.NecesitaNavegador;
import mx.unison.inventario.navegacion.Navegador;

import java.util.List;
import java.util.Optional;

/**
 * Controlador JavaFX de la vista de gestión de productos.
 *
 * <p>Muestra la lista de productos en un {@link TableView} y permite
 * crear, editar y eliminar registros. El formulario incluye un
 * {@link ComboBox} que carga dinámicamente los almacenes disponibles.
 * Toda la lógica de negocio se delega al {@link ProductoControlador}.</p>
 *
 * @author Sistema de Inventario v2 — UNISON
 * @version 2.0
 */
public class ProductosControlador implements NecesitaNavegador {

    @FXML private TableView<ProductoModel>          tablaProductos;
    @FXML private TableColumn<ProductoModel, Integer> colId;
    @FXML private TableColumn<ProductoModel, String>  colNombre;
    @FXML private TableColumn<ProductoModel, String>  colDescripcion;
    @FXML private TableColumn<ProductoModel, Integer> colCantidad;
    @FXML private TableColumn<ProductoModel, Double>  colPrecio;
    @FXML private TableColumn<ProductoModel, String>  colAlmacen;
    @FXML private TableColumn<ProductoModel, String>  colUsuario;
    @FXML private TextField campoBusqueda;
    @FXML private Label     labelConteo;
    @FXML private TableColumn<ProductoModel, String> colCreado;

    private Navegador navegador;
    private final ProductoControlador controlador = new ProductoControlador();
    private final AlmacenControlador  almControlador = new AlmacenControlador();

    /** Inicializa columnas y carga datos al abrir la vista. */
    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        // Columna almacén con valor derivado
        colAlmacen.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getNombreAlmacen()));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("ultimoUsuario"));
        colCreado.setCellValueFactory(new PropertyValueFactory<>("fechaHoraCreacion"));
        cargarDatos();

        // Búsqueda en tiempo real
        campoBusqueda.textProperty().addListener((o, a, b) -> filtrar(b));
    }

    @Override
    public void setNavegador(Navegador navegador) { this.navegador = navegador; }

    @FXML
    public void irInicio() {
        navegador.navegar("INICIO");
    }

    @FXML
    public void irProductos() {
        navegador.navegar("PRODUCTOS");
    }

    @FXML
    public void irAlmacenes() {
        navegador.navegar("ALMACENES");
    }

    @FXML
    public void cerrarSesion() {
        navegador.invalidarTodaLaCache();
        navegador.navegar("LOGIN");
    }

    /** Regresa al menú principal. */
    @FXML public void regresar() { navegador.navegar("INICIO"); }

    /** Abre el diálogo para agregar un nuevo producto. */
    @FXML
    public void agregar() {
        List<AlmacenModel> almacenes = almControlador.listarAlmacenes();
        mostrarDialogo(null, almacenes).ifPresent(campos -> {
            try {
                controlador.crear(campos.nombre, campos.descripcion,
                                  campos.cantidad, campos.precio, campos.almacen);
                cargarDatos();
            } catch (IllegalArgumentException e) {
                mostrarError(e.getMessage());
            }
        });
    }

    /** Abre el diálogo para editar el producto seleccionado. */
    @FXML
    public void editar() {
        ProductoModel sel = tablaProductos.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAviso("Selecciona un producto para editar."); return; }
        List<AlmacenModel> almacenes = almControlador.listarAlmacenes();
        mostrarDialogo(sel, almacenes).ifPresent(campos -> {
            try {
                controlador.actualizar(sel, campos.nombre, campos.descripcion,
                                       campos.cantidad, campos.precio, campos.almacen);
                cargarDatos();
            } catch (IllegalArgumentException e) {
                mostrarError(e.getMessage());
            }
        });
    }

    /** Elimina el producto seleccionado tras confirmación. */
    @FXML
    public void eliminar() {
        ProductoModel sel = tablaProductos.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarAviso("Selecciona un producto para eliminar."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "¿Eliminar el producto \"" + sel.getNombre() + "\"?",
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
        var lista = controlador.listarProductos();
        tablaProductos.setItems(FXCollections.observableArrayList(lista));
        labelConteo.setText("Total: " + lista.size() + " producto(s)");
    }

    /** Filtra la tabla por texto de búsqueda. */
    private void filtrar(String texto) {
        var lista = controlador.buscar(texto);
        tablaProductos.setItems(FXCollections.observableArrayList(lista));
    }

    /** Datos capturados por el diálogo de formulario. */
    private static class CamposProducto {
        String nombre, descripcion;
        int cantidad;
        double precio;
        AlmacenModel almacen;
    }

    /**
     * Muestra el diálogo de formulario para crear o editar un producto.
     *
     * @param producto  producto existente para edición, o {@code null} para creación
     * @param almacenes lista de almacenes disponibles para el combo
     * @return {@link Optional} con los campos capturados si el usuario aceptó
     */
    private Optional<CamposProducto> mostrarDialogo(ProductoModel producto,
                                                     List<AlmacenModel> almacenes) {
        Dialog<CamposProducto> dialog = new Dialog<>();
        dialog.setTitle(producto == null ? "Nuevo Producto" : "Editar Producto");
        dialog.setHeaderText(null);

        TextField fNombre      = new TextField(producto != null ? producto.getNombre() : "");
        TextField fDescripcion = new TextField(producto != null ? producto.getDescripcion() : "");
        TextField fCantidad    = new TextField(producto != null ? String.valueOf(producto.getCantidad()) : "");
        TextField fPrecio      = new TextField(producto != null ? String.valueOf(producto.getPrecio()) : "");
        ComboBox<AlmacenModel> cbAlmacen = new ComboBox<>(FXCollections.observableArrayList(almacenes));
        cbAlmacen.setConverter(new javafx.util.StringConverter<>() {
            public String toString(AlmacenModel a) { return a == null ? "— Sin almacén —" : a.getNombre(); }
            public AlmacenModel fromString(String s) { return null; }
        });
        if (producto != null && producto.getAlmacen() != null) {
            almacenes.stream().filter(a -> a.getId() == producto.getAlmacen().getId())
                     .findFirst().ifPresent(cbAlmacen::setValue);
        }

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(16));
        grid.add(new Label("Nombre *:"),    0, 0); grid.add(fNombre,      1, 0);
        grid.add(new Label("Descripción:"), 0, 1); grid.add(fDescripcion, 1, 1);
        grid.add(new Label("Cantidad *:"),  0, 2); grid.add(fCantidad,    1, 2);
        grid.add(new Label("Precio *:"),    0, 3); grid.add(fPrecio,      1, 3);
        grid.add(new Label("Almacén:"),     0, 4); grid.add(cbAlmacen,    1, 4);
        dialog.getDialogPane().setContent(grid);

        ButtonType btnOk = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnOk, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> {
            if (bt != btnOk) return null;
            try {
                CamposProducto c = new CamposProducto();
                c.nombre      = fNombre.getText();
                c.descripcion = fDescripcion.getText();
                c.cantidad    = Integer.parseInt(fCantidad.getText().trim());
                c.precio      = Double.parseDouble(fPrecio.getText().trim());
                c.almacen     = cbAlmacen.getValue();
                return c;
            } catch (NumberFormatException e) {
                mostrarError("Cantidad debe ser entero y precio debe ser número.");
                return null;
            }
        });
        return dialog.showAndWait();
    }

    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
    private void mostrarAviso(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }
}
