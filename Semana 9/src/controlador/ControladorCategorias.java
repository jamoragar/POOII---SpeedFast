package controlador;
import modelo.*;
import java.sql.SQLException;
import java.util.List;
public final class ControladorCategorias extends ControladorCRUD<Categoria> {
    public ControladorCategorias(Sesion sesion) { super(sesion); }
    @Override public List<Categoria> listar() throws SQLException { return biblioteca.categorias(sesion); }
    @Override public int crear(Categoria e) throws SQLException { return biblioteca.crearCategoria(sesion,e); }
    @Override public void actualizar(Categoria original,Categoria e) throws SQLException { biblioteca.actualizarCategoria(sesion,e); }
    @Override public void eliminar(int id) throws SQLException { biblioteca.eliminarCategoria(sesion,id); }

}
