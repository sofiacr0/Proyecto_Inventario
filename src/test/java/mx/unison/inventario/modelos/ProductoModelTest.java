package mx.unison.inventario.modelos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de ProductoModel")
class ProductoModelTest {

    private ProductoModel producto;
    private AlmacenModel almacen;

    @BeforeEach
    void setUp() {
        producto = new ProductoModel();
        almacen = new AlmacenModel("Almacén Test", "Ubicación Test");
    }

    @Nested
    @DisplayName("✓ Constructor sin argumentos")
    class ConstructorSinArgs {
        @Test
        void debeCrearInstanciaVacia() {
            ProductoModel modelo = new ProductoModel();
            assertNotNull(modelo);
            assertNull(modelo.getNombre());
            assertEquals(0, modelo.getCantidad());
            assertEquals(0.0, modelo.getPrecio());
        }
    }

    @Nested
    @DisplayName("✓ Constructor con argumentos")
    class ConstructorConArgs {
        @Test
        void debeInicializarCampos() {
            ProductoModel modelo = new ProductoModel("Laptop", 10, 999.99);
            assertEquals("Laptop", modelo.getNombre());
            assertEquals(10, modelo.getCantidad());
            assertEquals(999.99, modelo.getPrecio());
        }

        @Test
        void debeAceptarCantidadCero() {
            ProductoModel modelo = new ProductoModel("Producto", 0, 50.0);
            assertEquals(0, modelo.getCantidad());
        }

        @Test
        void debeAceptarPrecioCero() {
            ProductoModel modelo = new ProductoModel("Producto", 5, 0.0);
            assertEquals(0.0, modelo.getPrecio());
        }
    }

    @Nested
    @DisplayName("✓ Getters y Setters")
    class GettersSetters {

        @Test
        void getNombreDebeRetornarNombre() {
            producto.setNombre("Monitor 24 pulgadas");
            assertEquals("Monitor 24 pulgadas", producto.getNombre());
        }

        @Test
        void getDescripcionDebeRetornarDescripcion() {
            producto.setDescripcion("Monitor LED Full HD");
            assertEquals("Monitor LED Full HD", producto.getDescripcion());
        }

        @Test
        void getCantidadDebeRetornarCantidad() {
            producto.setCantidad(25);
            assertEquals(25, producto.getCantidad());
        }

        @Test
        void getPrecioDebeRetornarPrecio() {
            producto.setPrecio(199.99);
            assertEquals(199.99, producto.getPrecio());
        }

        @Test
        void getAlmacenDebeRetornarAlmacen() {
            producto.setAlmacen(almacen);
            assertEquals(almacen, producto.getAlmacen());
        }
    }

    @Nested
    @DisplayName("✓ getNombreAlmacen()")
    class GetNombreAlmacen {

        @Test
        void debeRetornarNombreAlmacen() {
            producto.setAlmacen(almacen);
            assertEquals("Almacén Test", producto.getNombreAlmacen());
        }

        @Test
        void debeRetornarGuionCuandoAlmacenNulo() {
            producto.setAlmacen(null);
            assertEquals("—", producto.getNombreAlmacen());
        }

        @Test
        void debeManejarAlmacenSinNombre() {
            AlmacenModel almacenSinNombre = new AlmacenModel();
            producto.setAlmacen(almacenSinNombre);
            assertEquals("—", producto.getNombreAlmacen());
        }
    }

    @Nested
    @DisplayName("✓ Validaciones")
    class Validaciones {

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 100, 1000})
        void debeAceptarCantidadesValidas(int cantidad) {
            producto.setCantidad(cantidad);
            assertEquals(cantidad, producto.getCantidad());
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.0, 0.01, 100.50, 9999.99})
        void debeAceptarPreciosValidos(double precio) {
            producto.setPrecio(precio);
            assertEquals(precio, producto.getPrecio());
        }

        @Test
        void debeAceptarNombreNulo() {
            producto.setNombre(null);
            assertNull(producto.getNombre());
        }

        @Test
        void debeAceptarDescripcionNula() {
            producto.setDescripcion(null);
            assertNull(producto.getDescripcion());
        }
    }

    @Nested
    @DisplayName("✓ toString()")
    class ToStringTests {

        @Test
        void debeGenerarStringCorrecto() {
            producto.setNombre("Teclado Mecánico");
            producto.setCantidad(15);
            producto.setPrecio(79.99);

            String toString = producto.toString();
            assertTrue(toString.contains("ProductoModel"));
            assertTrue(toString.contains("Teclado Mecánico"));
            assertTrue(toString.contains("15"));
            assertTrue(toString.contains("79.99"));
        }

        @Test
        void debeGenerarStringConValoresNulos() {
            producto.setNombre(null);
            String toString = producto.toString();
            assertNotNull(toString);
            assertTrue(toString.contains("ProductoModel"));
        }
    }

    @Nested
    @DisplayName("✓ Casos de uso reales")
    class CasosReales {

        @Test
        void crearProductoCompleto() {
            ProductoModel prod = new ProductoModel("Mouse Inalámbrico", 50, 29.99);
            prod.setDescripcion("Mouse USB inalámbrico");
            prod.setAlmacen(almacen);
            prod.setFechaHoraCreacion("2026-04-26T10:00:00Z");

            assertEquals("Mouse Inalámbrico", prod.getNombre());
            assertEquals(50, prod.getCantidad());
            assertEquals(29.99, prod.getPrecio());
            assertEquals("Almacén Test", prod.getNombreAlmacen());
        }

        @Test
        void actualizarProducto() {
            ProductoModel prod = new ProductoModel("Producto Antiguo", 5, 50.0);
            prod.setNombre("Producto Nuevo");
            prod.setCantidad(10);
            prod.setPrecio(75.0);
            prod.setFechaHoraUltimaMod("2026-04-26T14:30:00Z");

            assertEquals("Producto Nuevo", prod.getNombre());
            assertEquals(10, prod.getCantidad());
            assertEquals(75.0, prod.getPrecio());
        }

        @Test
        void cambiarAlmacenProducto() {
            AlmacenModel almacen1 = new AlmacenModel("Almacén 1", "Ubicación 1");
            AlmacenModel almacen2 = new AlmacenModel("Almacén 2", "Ubicación 2");

            ProductoModel prod = new ProductoModel("Producto", 20, 100.0);
            prod.setAlmacen(almacen1);
            assertEquals("Almacén 1", prod.getNombreAlmacen());

            prod.setAlmacen(almacen2);
            assertEquals("Almacén 2", prod.getNombreAlmacen());
        }

        @ParameterizedTest
        @CsvSource({
                "Producto A,10,50.00",
                "Producto B,20,100.00",
                "Producto C,30,150.00"
        })
        void crearMultiplesProductos(String nombre, int cantidad, double precio) {
            ProductoModel prod = new ProductoModel(nombre, cantidad, precio);
            assertEquals(nombre, prod.getNombre());
            assertEquals(cantidad, prod.getCantidad());
            assertEquals(precio, prod.getPrecio());
        }
    }

    @Nested
    @DisplayName("✓ Pruebas con múltiples instancias")
    class MultiplesInstancias {

        @Test
        void cadaInstanciaDebeSerIndependiente() {
            ProductoModel prod1 = new ProductoModel("Producto 1", 10, 50.0);
            ProductoModel prod2 = new ProductoModel("Producto 2", 20, 100.0);

            assertEquals("Producto 1", prod1.getNombre());
            assertEquals("Producto 2", prod2.getNombre());
            assertNotEquals(prod1.getCantidad(), prod2.getCantidad());
        }
    }
}