package controlador;
import modelo.*;
import servicio.PrestamoServicio;
import java.sql.SQLException;
import java.util.List;
public final class ControladorPrestamos extends ControladorCRUD<Prestamo> {
    private final PrestamoServicio servicio=PrestamoServicio.getInstance();
    public ControladorPrestamos(Sesion sesion) { super(sesion); }
    @Override public List<Prestamo> listar() throws SQLException { return biblioteca.prestamos(sesion); }
    @Override public int crear(Prestamo e) throws SQLException { return servicio.registrar(sesion,e); }
    @Override public void actualizar(Prestamo original,Prestamo e) throws SQLException { servicio.actualizar(sesion,e); }
    @Override public void eliminar(int id) throws SQLException { servicio.eliminar(sesion,id); }
    public void devolver(int id) throws SQLException { servicio.devolver(sesion,id); }
    public List<Libro> libros() throws SQLException { return biblioteca.libros(sesion); }
    public List<Estudiante> estudiantes() throws SQLException { return biblioteca.estudiantes(sesion); }
}
