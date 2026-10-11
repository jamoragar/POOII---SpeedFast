package controlador;
import modelo.*;
import java.sql.SQLException;
import java.util.List;
public final class ControladorLibros extends ControladorCRUD<Libro> {
    public ControladorLibros(Sesion sesion) { super(sesion); }
    @Override public List<Libro> listar() throws SQLException { return biblioteca.libros(sesion); }
    @Override public int crear(Libro e) throws SQLException { return biblioteca.crearLibro(sesion,e); }
    @Override public void actualizar(Libro original,Libro e) throws SQLException { biblioteca.actualizarLibro(sesion,original,e); }
    @Override public void eliminar(int id) throws SQLException { biblioteca.eliminarLibro(sesion,id); }

    public List<Categoria> categorias() throws SQLException { return biblioteca.categorias(sesion); }
}
