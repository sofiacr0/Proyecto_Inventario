package mx.unison.inventario.modelos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de UsuarioModel")
class UsuarioModelTest {

    private UsuarioModel usuario;

    @BeforeEach
    void setUp() {
        usuario = new UsuarioModel();
    }

    @Nested
    @DisplayName("✓ Constructor sin argumentos")
    class ConstructorSinArgs {
        @Test
        void debeCrearInstanciaVacia() {
            UsuarioModel modelo = new UsuarioModel();
            assertNotNull(modelo);
            assertNull(modelo.getNombre());
            assertNull(modelo.getPasswordHash());
            assertNull(modelo.getRol());
        }
    }

    @Nested
    @DisplayName("✓ Constructor con argumentos")
    class ConstructorConArgs {
        @Test
        void debeInicializarCampos() {
            UsuarioModel modelo = new UsuarioModel("sofia", "5d41402abc4b2a76b9719d911017c592", "ADMIN");
            assertEquals("sofia", modelo.getNombre());
            assertEquals("5d41402abc4b2a76b9719d911017c592", modelo.getPasswordHash());
            assertEquals("ADMIN", modelo.getRol());
        }

        @Test
        void debeAceptarHashVacio() {
            UsuarioModel modelo = new UsuarioModel("usuario", "", "USER");
            assertEquals("", modelo.getPasswordHash());
        }
    }

    @Nested
    @DisplayName("✓ Getters y Setters")
    class GettersSetters {

        @Test
        void getNombreDebeRetornarNombre() {
            usuario.setNombre("juan");
            assertEquals("juan", usuario.getNombre());
        }

        @Test
        void getPasswordHashDebeRetornarHash() {
            String hash = "5d41402abc4b2a76b9719d911017c592";
            usuario.setPasswordHash(hash);
            assertEquals(hash, usuario.getPasswordHash());
        }

        @Test
        void getRolDebeRetornarRol() {
            usuario.setRol("PRODUCTOS");
            assertEquals("PRODUCTOS", usuario.getRol());
        }

        @Test
        void getUltimoInicioSesionDebeRetornarTimestamp() {
            String timestamp = "2026-04-26T10:30:00Z";
            usuario.setUltimoInicioSesion(timestamp);
            assertEquals(timestamp, usuario.getUltimoInicioSesion());
        }
    }

    @Nested
    @DisplayName("✓ Validaciones de roles")
    class ValidacionesRoles {

        @ParameterizedTest
        @ValueSource(strings = {"ADMIN", "PRODUCTOS", "ALMACENES", "USER", "VIEWER"})
        void debeAceptarRolesValidos(String rol) {
            usuario.setRol(rol);
            assertEquals(rol, usuario.getRol());
        }

        @Test
        void debePermitirRolEnMinusculas() {
            usuario.setRol("admin");
            assertEquals("admin", usuario.getRol());
        }
    }

    @Nested
    @DisplayName("✓ Validaciones de nombres")
    class ValidacionesNombres {

        @ParameterizedTest
        @ValueSource(strings = {"sofia", "admin123", "usuario_123", "a", "usuario.correo@dominio.com"})
        void debeAceptarDiversosNombres(String nombre) {
            usuario.setNombre(nombre);
            assertEquals(nombre, usuario.getNombre());
        }

        @Test
        void debeAceptarNombreVacio() {
            usuario.setNombre("");
            assertEquals("", usuario.getNombre());
        }

        @Test
        void debeAceptarNombreConEspacios() {
            usuario.setNombre("Juan Pérez");
            assertEquals("Juan Pérez", usuario.getNombre());
        }
    }

    @Nested
    @DisplayName("��� Validaciones de hash")
    class ValidacionesHash {

        @ParameterizedTest
        @ValueSource(strings = {
                "5d41402abc4b2a76b9719d911017c592",
                "21232f297a57a5a743894a0e4a801fc3",
                "e99a18c428cb38d5f260853678922e03"
        })
        void debeAceptarHashMD5Validos(String hash) {
            usuario.setPasswordHash(hash);
            assertEquals(hash, usuario.getPasswordHash());
        }

        @Test
        void debeAceptarHashVacio() {
            usuario.setPasswordHash("");
            assertEquals("", usuario.getPasswordHash());
        }
    }

    @Nested
    @DisplayName("✓ Timestamps de sesión")
    class TimestampsSesion {

        @Test
        void debeAceptarTimestampValido() {
            String timestamp = "2026-04-26T15:45:30Z";
            usuario.setUltimoInicioSesion(timestamp);
            assertEquals(timestamp, usuario.getUltimoInicioSesion());
        }

        @Test
        void debeAceptarTimestampNulo() {
            usuario.setUltimoInicioSesion(null);
            assertNull(usuario.getUltimoInicioSesion());
        }

        @Test
        void debePermitirMultiplesActualizaciones() {
            usuario.setUltimoInicioSesion("2026-04-26T10:00:00Z");
            assertEquals("2026-04-26T10:00:00Z", usuario.getUltimoInicioSesion());

            usuario.setUltimoInicioSesion("2026-04-26T15:30:00Z");
            assertEquals("2026-04-26T15:30:00Z", usuario.getUltimoInicioSesion());
        }
    }

    @Nested
    @DisplayName("✓ toString()")
    class ToStringTests {

        @Test
        void debeGenerarStringCorrecto() {
            usuario.setNombre("sofia");
            usuario.setRol("ADMIN");

            String toString = usuario.toString();
            assertTrue(toString.contains("UsuarioModel"));
            assertTrue(toString.contains("sofia"));
            assertTrue(toString.contains("ADMIN"));
        }

        @Test
        void debeGenerarStringConValoresNulos() {
            usuario.setNombre(null);
            usuario.setRol(null);

            String toString = usuario.toString();
            assertNotNull(toString);
            assertTrue(toString.contains("UsuarioModel"));
        }
    }

    @Nested
    @DisplayName("✓ Casos de uso reales")
    class CasosReales {

        @Test
        void crearUsuarioAdmin() {
            UsuarioModel admin = new UsuarioModel("ADMIN", "5d41402abc4b2a76b9719d911017c592", "ADMIN");
            assertEquals("ADMIN", admin.getNombre());
            assertEquals("ADMIN", admin.getRol());
            assertNotNull(admin.getPasswordHash());
        }

        @Test
        void crearUsuarioProductos() {
            UsuarioModel user = new UsuarioModel("PRODUCTOS", "hash123", "PRODUCTOS");
            assertEquals("PRODUCTOS", user.getNombre());
            assertEquals("PRODUCTOS", user.getRol());
        }

        @Test
        void actualizarUltimaSesion() {
            usuario.setNombre("usuario");
            usuario.setPasswordHash("hash");
            usuario.setRol("USER");

            assertNull(usuario.getUltimoInicioSesion());
            usuario.setUltimoInicioSesion("2026-04-26T12:00:00Z");
            assertNotNull(usuario.getUltimoInicioSesion());
        }

        @Test
        void cambiarContraseña() {
            usuario.setNombre("sofia");
            String hashAntiguo = "hash_antiguo";
            String hashNuevo = "hash_nuevo";

            usuario.setPasswordHash(hashAntiguo);
            assertEquals(hashAntiguo, usuario.getPasswordHash());

            usuario.setPasswordHash(hashNuevo);
            assertEquals(hashNuevo, usuario.getPasswordHash());
            assertNotEquals(hashAntiguo, usuario.getPasswordHash());
        }

        @ParameterizedTest
        @CsvSource({
                "admin,hash1,ADMIN",
                "productos,hash2,PRODUCTOS",
                "almacenes,hash3,ALMACENES"
        })
        void crearMultiplesUsuarios(String nombre, String hash, String rol) {
            UsuarioModel user = new UsuarioModel(nombre, hash, rol);
            assertEquals(nombre, user.getNombre());
            assertEquals(rol, user.getRol());
        }
    }

    @Nested
    @DisplayName("✓ Pruebas con múltiples instancias")
    class MultiplesInstancias {

        @Test
        void cadaInstanciaDebeSerIndependiente() {
            UsuarioModel usuario1 = new UsuarioModel("admin", "hash1", "ADMIN");
            UsuarioModel usuario2 = new UsuarioModel("user", "hash2", "USER");

            assertEquals("admin", usuario1.getNombre());
            assertEquals("user", usuario2.getNombre());
            assertNotEquals(usuario1.getNombre(), usuario2.getNombre());
        }
    }
}