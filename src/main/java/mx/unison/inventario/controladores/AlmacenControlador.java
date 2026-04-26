package mx.unison.inventario.controladores;

import mx.unison.inventario.datos.AlmacenDao;
import mx.unison.inventario.datos.ConexionDB;
import mx.unison.inventario.modelos.AlmacenModel;
import mx.unison.inventario.modelos.SesionUsuario;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador de dominio para la gestión de almacenes.
 *
 * <p>Contiene toda la lógica de negocio relacionada con almacenes:
 * validaciones, obtención del usuario de sesión y coordinación con
 * el {@link AlmacenDao}. La capa de vista delega aquí todas las
 * operaciones CRUD.</p>
 *
 * @author Sistema de Inventario v2 — UNISON
 * @version 2.0
 */
public class AlmacenControlador {

    private static final Logger LOG = Logger.getLogger(AlmacenControlador.class.getName());

    /** DAO de almacenes. */
    private final AlmacenDao almacenDao;

    /**
     * Construye el controlador obteniendo el DAO desde la conexión global.
     */
    public AlmacenControlador() {
        try {
            this.almacenDao = new AlmacenDao(ConexionDB.getInstancia().getConnectionSource());
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al crear AlmacenDao", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor para inyección de dependencias en pruebas.
     *
     * @param almacenDao DAO de almacenes a utilizar
     */
    public AlmacenControlador(AlmacenDao almacenDao) {
        this.almacenDao = almacenDao;
    }

    /**
     * Retorna todos los almacenes registrados.
     *
     * @return lista de almacenes; nunca {@code null}
     */
    public List<AlmacenModel> listarAlmacenes() {
        return almacenDao.listarTodos();
    }

    /**
     * Crea un nuevo almacén previa validación del nombre.
     *
     * @param nombre    nombre del almacén; no puede ser nulo ni vacío
     * @param ubicacion ubicación; puede ser nula o vacía
     * @return el {@link AlmacenModel} creado con su ID generado
     * @throws IllegalArgumentException si el nombre es inválido
     * @throws RuntimeException         si ocurre un error de persistencia
     */
    public AlmacenModel crear(String nombre, String ubicacion) {
        validarNombre(nombre);
        try {
            AlmacenModel a = new AlmacenModel(nombre.trim(), ubicacion != null ? ubicacion.trim() : "");
            return almacenDao.insertar(a, SesionUsuario.getInstancia().getNombreUsuario());
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al crear almacén", e);
            throw new RuntimeException("No se pudo crear el almacén", e);
        }
    }

    /**
     * Actualiza un almacén existente previa validación.
     *
     * @param almacen   almacén con los nuevos valores;
     *                  {@link AlmacenModel#getId()} debe corresponder a
     *                  un registro existente
     * @param nombre    nuevo nombre del almacén
     * @param ubicacion nueva ubicación
     * @throws IllegalArgumentException si el nombre es inválido
     * @throws RuntimeException         si ocurre un error de persistencia
     */
    public void actualizar(AlmacenModel almacen, String nombre, String ubicacion) {
        validarNombre(nombre);
        almacen.setNombre(nombre.trim());
        almacen.setUbicacion(ubicacion != null ? ubicacion.trim() : "");
        try {
            almacenDao.actualizar(almacen, SesionUsuario.getInstancia().getNombreUsuario());
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al actualizar almacén id=" + almacen.getId(), e);
            throw new RuntimeException("No se pudo actualizar el almacén", e);
        }
    }

    /**
     * Elimina el almacén con el ID indicado.
     *
     * @param id identificador del almacén a eliminar
     * @throws RuntimeException si ocurre un error de persistencia
     */
    public void eliminar(int id) {
        try {
            almacenDao.eliminar(id);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al eliminar almacén id=" + id, e);
            throw new RuntimeException("No se pudo eliminar el almacén", e);
        }
    }

    /**
     * Busca un almacén por ID.
     *
     * @param id identificador del almacén
     * @return {@link Optional} con el almacén, o vacío si no existe
     */
    public Optional<AlmacenModel> buscarPorId(int id) {
        return almacenDao.buscarPorId(id);
    }

    /**
     * Valida que el nombre de almacén no sea nulo ni vacío.
     *
     * @param nombre nombre a validar
     * @throws IllegalArgumentException si el nombre es inválido
     */
    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del almacén no puede estar vacío.");
        }
    }
}
