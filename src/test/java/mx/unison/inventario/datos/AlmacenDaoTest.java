package mx.unison.inventario.datos;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import mx.unison.inventario.modelos.AlmacenModel;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AlmacenDaoTest {

    private ConnectionSource connectionSource;
    private AlmacenDao dao;

    @BeforeEach
    void setUp() throws Exception {
        connectionSource = new JdbcConnectionSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        TableUtils.createTableIfNotExists(connectionSource, AlmacenModel.class);
        dao = new AlmacenDao(connectionSource);
    }

    @AfterEach
    void tearDown() throws Exception {
        connectionSource.close();
    }

    // ========================
    // LISTAR
    // ========================

    @Test
    void listarTodos_debeEstarVacioInicialmente() {
        List<AlmacenModel> lista = dao.listarTodos();
        assertNotNull(lista);
        assertTrue(lista.isEmpty());
    }

    @Test
    void listarTodos_debeRetornarDatosInsertados() throws Exception {
        dao.insertar(new AlmacenModel("A1", "U1"), "admin");
        dao.insertar(new AlmacenModel("A2", "U2"), "admin");

        List<AlmacenModel> lista = dao.listarTodos();

        assertEquals(2, lista.size());
    }

    // ========================
    // BUSCAR
    // ========================

    @Test
    void buscarPorId_debeRetornarEmptySiNoExiste() {
        Optional<AlmacenModel> res = dao.buscarPorId(999);
        assertTrue(res.isEmpty());
    }

    @Test
    void buscarPorId_debeEncontrarRegistro() throws Exception {
        AlmacenModel a = dao.insertar(new AlmacenModel("Test", "Ub"), "admin");

        Optional<AlmacenModel> res = dao.buscarPorId(a.getId());

        assertTrue(res.isPresent());
        assertEquals("Test", res.get().getNombre());
    }

    // ========================
    // INSERTAR
    // ========================

    @Test
    void insertar_debeAsignarIdYUsuario() throws Exception {
        AlmacenModel a = dao.insertar(new AlmacenModel("Central", "Centro"), "sofia");

        assertNotNull(a.getId());
        assertEquals("sofia", a.getUltimoUsuario());
        assertNotNull(a.getFechaHoraCreacion());
    }

    @Test
    void insertar_debePermitirMultiples() throws Exception {
        for (int i = 0; i < 5; i++) {
            dao.insertar(new AlmacenModel("A" + i, "U" + i), "admin");
        }

        assertEquals(5, dao.listarTodos().size());
    }

    // ========================
    // ACTUALIZAR
    // ========================

    @Test
    void actualizar_debeModificarNombreYUsuario() throws Exception {
        AlmacenModel a = dao.insertar(new AlmacenModel("Viejo", "Ub"), "admin");

        a.setNombre("Nuevo");
        dao.actualizar(a, "sofia");

        AlmacenModel actualizado = dao.buscarPorId(a.getId()).orElseThrow();

        assertEquals("Nuevo", actualizado.getNombre());
        assertEquals("sofia", actualizado.getUltimoUsuario());
        assertNotNull(actualizado.getFechaHoraUltimaMod());
    }

    // ========================
    // ELIMINAR
    // ========================

    @Test
    void eliminar_debeEliminarRegistro() throws Exception {
        AlmacenModel a = dao.insertar(new AlmacenModel("A", "U"), "admin");

        dao.eliminar(a.getId());

        assertTrue(dao.buscarPorId(a.getId()).isEmpty());
    }

    @Test
    void eliminar_noDebeFallarSiNoExiste() {
        assertDoesNotThrow(() -> dao.eliminar(999));
    }

    // ========================
    // CONTAR
    // ========================

    @Test
    void contarTodos_debeSer0Inicialmente() {
        assertEquals(0, dao.contarTodos());
    }

    @Test
    void contarTodos_debeReflejarInserciones() throws Exception {
        dao.insertar(new AlmacenModel("A1", "U1"), "admin");
        dao.insertar(new AlmacenModel("A2", "U2"), "admin");

        assertEquals(2, dao.contarTodos());
    }

    @Test
    void contarTodos_debeDisminuirAlEliminar() throws Exception {
        AlmacenModel a = dao.insertar(new AlmacenModel("A", "U"), "admin");

        dao.eliminar(a.getId());

        assertEquals(0, dao.contarTodos());
    }
}