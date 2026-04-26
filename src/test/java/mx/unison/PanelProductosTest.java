package mx.unison;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PanelProductosTest {

    static Database db;

    @BeforeAll
    static void setup() {
        db = new Database();
    }

    // ─── VERIFICAR QUE LA LISTA CARGA CORRECTAMENTE ──────────────

    @Test
    @Order(1)
    void testListaProductosNoEsNula() {
        List<Producto> lista = db.listProductos();
        assertNotNull(lista, "La lista de productos no debe ser nula");
    }

    // ─── AGREGAR PRODUCTO (simula el botón Agregar) ───────────────

    @Test
    @Order(2)
    void testAgregarProducto() {
        Producto p     = new Producto();
        p.nombre       = "Producto Panel Test";
        p.descripcion  = "Descripción test";
        p.cantidad     = 5;
        p.precio       = 49.99;
        p.almacenId    = -1;
        int id = db.insertProducto(p, "ADMIN");
        assertTrue(id > 0, "Debe retornar un id válido al insertar");
    }

    @Test
    @Order(3)
    void testAgregarProductoApareceEnLista() {
        Producto p     = new Producto();
        p.nombre       = "Producto Visible";
        p.descripcion  = "visible en lista";
        p.cantidad     = 3;
        p.precio       = 29.99;
        p.almacenId    = -1;
        db.insertProducto(p, "ADMIN");
        List<Producto> lista = db.listProductos();
        boolean encontrado = lista.stream().anyMatch(x -> x.nombre.equals("Producto Visible"));
        assertTrue(encontrado, "El producto recién agregado debe aparecer en la lista");
    }

    // ─── MODIFICAR PRODUCTO (simula el botón Modificar) ───────────

    @Test
    @Order(4)
    void testModificarProducto() {
        Producto p     = new Producto();
        p.nombre       = "Producto Original";
        p.descripcion  = "antes de modificar";
        p.cantidad     = 10;
        p.precio       = 100.0;
        p.almacenId    = -1;
        int id = db.insertProducto(p, "ADMIN");

        p.id           = id;
        p.nombre       = "Producto Actualizado";
        p.cantidad     = 20;
        db.updateProducto(p, "ADMIN");

        List<Producto> lista = db.listProductos();
        boolean actualizado = lista.stream().anyMatch(x -> x.nombre.equals("Producto Actualizado"));
        assertTrue(actualizado, "El producto debe reflejar el nombre modificado");
    }

    @Test
    @Order(5)
    void testModificarProductoNombreAnteriorNoExiste() {
        Producto p     = new Producto();
        p.nombre       = "Nombre Viejo Producto";
        p.descripcion  = "temporal";
        p.cantidad     = 1;
        p.precio       = 1.0;
        p.almacenId    = -1;
        int id = db.insertProducto(p, "ADMIN");

        p.id           = id;
        p.nombre       = "Nombre Nuevo Producto";
        db.updateProducto(p, "ADMIN");

        List<Producto> lista = db.listProductos();
        boolean viejoExiste = lista.stream().anyMatch(x -> x.nombre.equals("Nombre Viejo Producto"));
        assertFalse(viejoExiste, "El nombre anterior no debe existir tras la modificación");
    }

    // ─── ELIMINAR PRODUCTO (simula el botón Eliminar) ─────────────

    @Test
    @Order(6)
    void testEliminarProducto() {
        Producto p     = new Producto();
        p.nombre       = "Producto a Eliminar";
        p.descripcion  = "temporal";
        p.cantidad     = 1;
        p.precio       = 1.0;
        p.almacenId    = -1;
        int id = db.insertProducto(p, "ADMIN");
        db.deleteProducto(id);
        List<Producto> lista = db.listProductos();
        boolean existe = lista.stream().anyMatch(x -> x.id == id);
        assertFalse(existe, "El producto eliminado no debe aparecer en la lista");
    }

    @Test
    @Order(7)
    void testEliminarProductoInexistente() {
        assertDoesNotThrow(() -> db.deleteProducto(99999),
                "Eliminar un id inexistente no debe lanzar excepción");
    }

    // ─── VALIDACIONES DE CAMPOS ───────────────────────────────────

    @Test
    @Order(8)
    void testProductoConCantidadCero() {
        Producto p     = new Producto();
        p.nombre       = "Producto Cantidad Cero";
        p.descripcion  = "test";
        p.cantidad     = 0;
        p.precio       = 10.0;
        p.almacenId    = -1;
        int id = db.insertProducto(p, "ADMIN");
        assertTrue(id > 0, "Debe permitir insertar un producto con cantidad 0");
    }

    @Test
    @Order(9)
    void testProductoConPrecioCero() {
        Producto p     = new Producto();
        p.nombre       = "Producto Precio Cero";
        p.descripcion  = "test";
        p.cantidad     = 1;
        p.precio       = 0.0;
        p.almacenId    = -1;
        int id = db.insertProducto(p, "ADMIN");
        assertTrue(id > 0, "Debe permitir insertar un producto con precio 0");
    }
}