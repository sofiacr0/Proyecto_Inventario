package mx.unison;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoginTest {

    static Database db;

    @BeforeAll
    static void setup() {
        db = new Database();
    }

    // ─── CREDENCIALES CORRECTAS ───────────────────────────────────

    @Test
    @Order(1)
    void testLoginAdminCorrecto() {
        Usuario u = db.authenticate("ADMIN", "admin23");
        assertNotNull(u, "ADMIN con contraseña correcta debe autenticarse");
    }

    @Test
    @Order(2)
    void testLoginProductosCorrecto() {
        Usuario u = db.authenticate("PRODUCTOS", "productos19");
        assertNotNull(u, "PRODUCTOS con contraseña correcta debe autenticarse");
    }

    @Test
    @Order(3)
    void testLoginAlmacenesCorrecto() {
        Usuario u = db.authenticate("ALMACENES", "almacenes11");
        assertNotNull(u, "ALMACENES con contraseña correcta debe autenticarse");
    }

    // ─── CREDENCIALES INCORRECTAS ─────────────────────────────────

    @Test
    @Order(4)
    void testLoginPasswordIncorrecta() {
        Usuario u = db.authenticate("ADMIN", "passwordMal");
        assertNull(u, "Con contraseña incorrecta debe regresar null");
    }

    @Test
    @Order(5)
    void testLoginUsuarioInexistente() {
        Usuario u = db.authenticate("INEXISTENTE", "admin23");
        assertNull(u, "Un usuario que no existe debe regresar null");
    }

    @Test
    @Order(6)
    void testLoginCamposVacios() {
        Usuario u = db.authenticate("", "");
        assertNull(u, "Con campos vacíos debe regresar null");
    }

    @Test
    @Order(7)
    void testLoginUsuarioVacio() {
        Usuario u = db.authenticate("", "admin23");
        assertNull(u, "Con usuario vacío debe regresar null");
    }

    @Test
    @Order(8)
    void testLoginPasswordVacia() {
        Usuario u = db.authenticate("ADMIN", "");
        assertNull(u, "Con contraseña vacía debe regresar null");
    }

    // ─── ROL ASIGNADO CORRECTAMENTE ──────────────────────────────

    @Test
    @Order(9)
    void testRolAdmin() {
        Usuario u = db.authenticate("ADMIN", "admin23");
        assertNotNull(u);
        assertEquals("ADMIN", u.rol, "El rol debe ser ADMIN");
    }

    @Test
    @Order(10)
    void testRolProductos() {
        Usuario u = db.authenticate("PRODUCTOS", "productos19");
        assertNotNull(u);
        assertEquals("PRODUCTOS", u.rol, "El rol debe ser PRODUCTOS");
    }

    @Test
    @Order(11)
    void testRolAlmacenes() {
        Usuario u = db.authenticate("ALMACENES", "almacenes11");
        assertNotNull(u);
        assertEquals("ALMACENES", u.rol, "El rol debe ser ALMACENES");
    }
}