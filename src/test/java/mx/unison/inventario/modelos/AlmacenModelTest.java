package mx.unison.inventario.modelos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de AlmacenModel")
class AlmacenModelTest {

    private AlmacenModel almacen;

    @BeforeEach
    void setUp() {
        almacen = new AlmacenModel();
    }

    @Nested
    @DisplayName("✓ Constructor sin argumentos")
    class ConstructorSinArgs {
        @Test
        void debeCrearInstanciaVacia() {
            AlmacenModel modelo = new AlmacenModel();
            assertNotNull(modelo);
            assertNull(modelo.getNombre());
            assertNull(modelo.getUbicacion());
        }
    }

    @Nested
    @DisplayName("✓ Constructor con argumentos")
    class ConstructorConArgs {
        @Test
        void debeInicializarNombreYUbicacion() {
            AlmacenModel modelo = new AlmacenModel("Almacén Principal", "Monterrey");
            assertEquals("Almacén Principal", modelo.getNombre());
            assertEquals("Monterrey", modelo.getUbicacion());
        }

        @Test
        void debeAceptarUbicacionNula() {
            AlmacenModel modelo = new AlmacenModel("Bodega", null);
            assertEquals("Bodega", modelo.getNombre());
            assertNull(modelo.getUbicacion());
        }
    }

    @Nested
    @DisplayName("✓ Getters y Setters")
    class GettersSetters {

        @Test
        void getNombreDebeRetornarNombre() {
            almacen.setNombre("Almacén Central");
            assertEquals("Almacén Central", almacen.getNombre());
        }

        @Test
        void getUbicacionDebeRetornarUbicacion() {
            almacen.setUbicacion("CDMX");
            assertEquals("CDMX", almacen.getUbicacion());
        }

        @Test
        void getFechaHoraCreacionDebeRetornarTimestamp() {
            String timestamp = "2026-04-26T10:30:00Z";
            almacen.setFechaHoraCreacion(timestamp);
            assertEquals(timestamp, almacen.getFechaHoraCreacion());
        }

        @Test
        void getFechaHoraUltimaModDebeRetornarTimestamp() {
            String timestamp = "2026-04-26T15:45:00Z";
            almacen.setFechaHoraUltimaMod(timestamp);
            assertEquals(timestamp, almacen.getFechaHoraUltimaMod());
        }

        @Test
        void getUltimoUsuarioDebeRetornarNombre() {
            almacen.setUltimoUsuario("sofia");
            assertEquals("sofia", almacen.getUltimoUsuario());
        }
    }

    @Nested
    @DisplayName("✓ Validaciones de entrada")
    class ValidacionesEntrada {

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "A", "Almacén de Prueba 123"})
        void debeAceptarDiversosNombres(String nombre) {
            almacen.setNombre(nombre);
            assertEquals(nombre, almacen.getNombre());
        }

        @Test
        void debeAceptarValoresNulos() {
            almacen.setNombre(null);
            almacen.setUbicacion(null);
            assertNull(almacen.getNombre());
            assertNull(almacen.getUbicacion());
        }
    }

    @Nested
    @DisplayName("✓ toString()")
    class ToStringTests {

        @Test
        void debeGenerarStringCorrecto() {
            almacen.setNombre("Bodega A");
            almacen.setUbicacion("Monterrey");

            String toString = almacen.toString();
            assertTrue(toString.contains("AlmacenModel"));
            assertTrue(toString.contains("Bodega A"));
            assertTrue(toString.contains("Monterrey"));
        }

        @Test
        void debeGenerarStringConValoresNulos() {
            almacen.setNombre(null);
            almacen.setUbicacion(null);

            String toString = almacen.toString();
            assertNotNull(toString);
            assertTrue(toString.contains("AlmacenModel"));
        }
    }

    @Nested
    @DisplayName("✓ Casos de uso reales")
    class CasosReales {

        @Test
        void crearAlmacenCompleto() {
            AlmacenModel almacen = new AlmacenModel("Bodega Principal", "Zona Industrial");
            almacen.setFechaHoraCreacion("2026-04-26T09:00:00Z");
            almacen.setUltimoUsuario("admin");

            assertEquals("Bodega Principal", almacen.getNombre());
            assertEquals("Zona Industrial", almacen.getUbicacion());
            assertNotNull(almacen.getFechaHoraCreacion());
            assertEquals("admin", almacen.getUltimoUsuario());
        }

        @Test
        void actualizarAlmacen() {
            almacen.setNombre("Almacén Antiguo");
            almacen.setUbicacion("Ubicación Antigua");

            almacen.setNombre("Almacén Nuevo");
            almacen.setUbicacion("Ubicación Nueva");
            almacen.setFechaHoraUltimaMod("2026-04-26T14:20:00Z");
            almacen.setUltimoUsuario("usuario");

            assertEquals("Almacén Nuevo", almacen.getNombre());
            assertEquals("Ubicación Nueva", almacen.getUbicacion());
            assertEquals("2026-04-26T14:20:00Z", almacen.getFechaHoraUltimaMod());
        }

        @ParameterizedTest
        @CsvSource({
                "Almacén A,Ubicación A",
                "Almacén B,Ubicación B",
                "Almacén C,Ubicación C"
        })
        void crearMultiplesAlmacenes(String nombre, String ubicacion) {
            AlmacenModel modelo = new AlmacenModel(nombre, ubicacion);
            assertEquals(nombre, modelo.getNombre());
            assertEquals(ubicacion, modelo.getUbicacion());
        }
    }

    @Nested
    @DisplayName("✓ Pruebas con múltiples instancias")
    class MultiplesInstancias {

        @Test
        void cadaInstanciaDebeSerIndependiente() {
            AlmacenModel almacen1 = new AlmacenModel("Almacén 1", "Ubicación 1");
            AlmacenModel almacen2 = new AlmacenModel("Almacén 2", "Ubicación 2");

            assertEquals("Almacén 1", almacen1.getNombre());
            assertEquals("Almacén 2", almacen2.getNombre());
            assertNotEquals(almacen1.getNombre(), almacen2.getNombre());
        }
    }
}