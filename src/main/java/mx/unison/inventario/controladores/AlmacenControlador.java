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

public class AlmacenControlador {

    private static final Logger LOG = Logger.getLogger(AlmacenControlador.class.getName());

    /** DAO de almacenes. */
    private final AlmacenDao almacenDao;

    public AlmacenControlador() {
        try {
            this.almacenDao = new AlmacenDao(ConexionDB.getInstancia().getConnectionSource());
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al crear AlmacenDao", e);
            throw new RuntimeException(e);
        }
    }

    public AlmacenControlador(AlmacenDao almacenDao) {
        this.almacenDao = almacenDao;
    }

    public List<AlmacenModel> listarAlmacenes() {
        return almacenDao.listarTodos();
    }

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

    public void eliminar(int id) {
        try {
            almacenDao.eliminar(id);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error al eliminar almacén id=" + id, e);
            throw new RuntimeException("No se pudo eliminar el almacén", e);
        }
    }

    public Optional<AlmacenModel> buscarPorId(int id) {
        return almacenDao.buscarPorId(id);
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del almacén no puede estar vacío.");
        }
    }
}
