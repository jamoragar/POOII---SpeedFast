package controlador;
import modelo.Entidad;
import modelo.Sesion;
import modelo.Accion;
import servicio.BibliotecaServicio;
import java.sql.SQLException;
import java.util.List;
/** Frontera entre formulario y negocio; los permisos efectivos se verifican en el servicio. */
public abstract class ControladorCRUD<T extends Entidad> {
    protected final Sesion sesion;
    protected final BibliotecaServicio biblioteca=BibliotecaServicio.getInstance();
    protected ControladorCRUD(Sesion sesion) { this.sesion=sesion; }
    public boolean esBibliotecario() { return sesion.usuario().puedeRealizar(Accion.ADMINISTRAR); }
    public abstract List<T> listar() throws SQLException;
    public abstract int crear(T entidad) throws SQLException;
    public abstract void actualizar(T original,T entidad) throws SQLException;
    public abstract void eliminar(int id) throws SQLException;
}
