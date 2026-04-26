package mx.unison;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseTest {

    static Database db;

    @BeforeAll
    static void setup() {
        db = new Database();
    }

    // ─── AUTENTICACIÓN ───────────────────────────────────────────

    @Test
    @Order(1)
    void testLoginCorrecto() {
        Usuario u = db.authenticate("ADMIN", "admin23");
        assertNotNull(u, "El usuario debería existir");
        assertEquals("ADMIN", u.nombre);
        assertEquals("ADMIN", u.rol);
    }

    @Test
    @Order(2)
    void testLoginIncorrecto() {
        Usuario u = db.authenticate("ADMIN", "passwordMal");
        assertNull(u, "Con credenciales incorrectas debe regresar null");
    }

    // ─── ALMACENES ───────────────────────────────────────────────

    @Test
    @Order(3)
    void testInsertAlmacen() {
        int id = db.insertAlmacen("Almacén Test", "Hermosillo", "ADMIN");
        assertTrue(id > 0, "El id generado debe ser mayor a 0");
    }

    @Test
    @Order(4)
    void testListAlmacenes() {
        List<Almacen> lista = db.listAlmacenes();
        assertFalse(lista.isEmpty(), "La lista de almacenes no debe estar vacía");
    }

    @Test
    @Order(5)
    void testUpdateAlmacen() {
        List<Almacen> lista = db.listAlmacenes();
        Almacen a = lista.get(0);
        db.updateAlmacen(a.id, "Almacén Modificado", "Sonora", "ADMIN");
        List<Almacen> actualizada = db.listAlmacenes();
        boolean encontrado = actualizada.stream().anyMatch(x -> x.nombre.equals("Almacén Modificado"));
        assertTrue(encontrado, "El almacén debería haberse actualizado");
    }

    @Test
    @Order(6)
    void testDeleteAlmacen() {
        int id = db.insertAlmacen("Almacén a Eliminar", "Nogales", "ADMIN");
        db.deleteAlmacen(id);
        List<Almacen> lista = db.listAlmacenes();
        boolean existe = lista.stream().anyMatch(a -> a.id == id);
        assertFalse(existe, "El almacén debería haberse eliminado");
    }

    // ─── PRODUCTOS ───────────────────────────────────────────────

    @Test
    @Order(7)
    void testInsertProducto() {
        Producto p = new Producto();
        p.nombre      = "Producto Test";
        p.descripcion = "Descripción de prueba";
        p.cantidad    = 10;
        p.precio      = 99.99;
        p.almacenId   = -1;
        int id = db.insertProducto(p, "ADMIN");
        assertTrue(id > 0, "El id generado debe ser mayor a 0");
    }

    @Test
    @Order(8)
    void testListProductos() {
        List<Producto> lista = db.listProductos();
        assertFalse(lista.isEmpty(), "La lista de productos no debe estar vacía");
    }

    @Test
    @Order(9)
    void testUpdateProducto() {
        List<Producto> lista = db.listProductos();
        Producto p = lista.get(0);
        p.nombre    = "Producto Modificado";
        p.cantidad  = 99;
        db.updateProducto(p, "ADMIN");
        List<Producto> actualizada = db.listProductos();
        boolean encontrado = actualizada.stream().anyMatch(x -> x.nombre.equals("Producto Modificado"));
        assertTrue(encontrado, "El producto debería haberse actualizado");
    }

    @Test
    @Order(10)
    void testDeleteProducto() {
        Producto p = new Producto();
        p.nombre      = "Producto a Eliminar";
        p.descripcion = "temporal";
        p.cantidad    = 1;
        p.precio      = 1.0;
        p.almacenId   = -1;
        int id = db.insertProducto(p, "ADMIN");
        db.deleteProducto(id);
        List<Producto> lista = db.listProductos();
        boolean existe = lista.stream().anyMatch(x -> x.id == id);
        assertFalse(existe, "El producto debería haberse eliminado");
    }
}