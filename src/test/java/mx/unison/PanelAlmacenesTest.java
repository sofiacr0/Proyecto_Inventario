package mx.unison;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PanelAlmacenesTest {

    static Database db;

    @BeforeAll
    static void setup() {
        db = new Database();
    }

    // ─── VERIFICAR QUE LA LISTA CARGA CORRECTAMENTE ──────────────

    @Test
    @Order(1)
    void testListaAlmacenesNoEsNula() {
        List<Almacen> lista = db.listAlmacenes();
        assertNotNull(lista, "La lista de almacenes no debe ser nula");
    }

    // ─── AGREGAR ALMACÉN (simula el botón Agregar) ───────────────

    @Test
    @Order(2)
    void testAgregarAlmacen() {
        int id = db.insertAlmacen("Almacén Panel Test", "Hermosillo", "ADMIN");
        assertTrue(id > 0, "Debe retornar un id válido al insertar");
    }

    @Test
    @Order(3)
    void testAgregarAlmacenApareceEnLista() {
        db.insertAlmacen("Almacén Visible", "Sonora", "ADMIN");
        List<Almacen> lista = db.listAlmacenes();
        boolean encontrado = lista.stream().anyMatch(a -> a.nombre.equals("Almacén Visible"));
        assertTrue(encontrado, "El almacén recién agregado debe aparecer en la lista");
    }

    // ─── MODIFICAR ALMACÉN (simula el botón Modificar) ───────────

    @Test
    @Order(4)
    void testModificarAlmacen() {
        int id = db.insertAlmacen("Almacén Original", "Guaymas", "ADMIN");
        db.updateAlmacen(id, "Almacén Actualizado", "Obregón", "ADMIN");
        List<Almacen> lista = db.listAlmacenes();
        boolean actualizado = lista.stream().anyMatch(a -> a.nombre.equals("Almacén Actualizado"));
        assertTrue(actualizado, "El almacén debe reflejar el nombre modificado");
    }

    @Test
    @Order(5)
    void testModificarAlmacenNombreAnteriorNoExiste() {
        int id = db.insertAlmacen("Nombre Viejo", "Caborca", "ADMIN");
        db.updateAlmacen(id, "Nombre Nuevo", "Caborca", "ADMIN");
        List<Almacen> lista = db.listAlmacenes();
        boolean viejoExiste = lista.stream().anyMatch(a -> a.nombre.equals("Nombre Viejo"));
        assertFalse(viejoExiste, "El nombre anterior no debe existir tras la modificación");
    }

    // ─── ELIMINAR ALMACÉN (simula el botón Eliminar) ─────────────

    @Test
    @Order(6)
    void testEliminarAlmacen() {
        int id = db.insertAlmacen("Almacén a Eliminar", "Navojoa", "ADMIN");
        db.deleteAlmacen(id);
        List<Almacen> lista = db.listAlmacenes();
        boolean existe = lista.stream().anyMatch(a -> a.id == id);
        assertFalse(existe, "El almacén eliminado no debe aparecer en la lista");
    }

    @Test
    @Order(7)
    void testEliminarAlmacenInexistente() {
        assertDoesNotThrow(() -> db.deleteAlmacen(99999),
                "Eliminar un id inexistente no debe lanzar excepción");
    }
}