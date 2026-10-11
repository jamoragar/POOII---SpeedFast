package controlador;
import modelo.*;
import java.sql.SQLException;
import java.util.List;
public final class ControladorUsuarios extends ControladorCRUD<Usuario> {
    public ControladorUsuarios(Sesion sesion) { super(sesion); }
    @Override public List<Usuario> listar() throws SQLException { return biblioteca.usuarios(sesion); }
    @Override public int crear(Usuario e) throws SQLException { throw new IllegalArgumentException("Debes proporcionar la contraseña inicial."); }
    @Override public void actualizar(Usuario original,Usuario e) throws SQLException { biblioteca.actualizarUsuario(sesion,e,new char[0]); }
    @Override public void eliminar(int id) throws SQLException { biblioteca.eliminarUsuario(sesion,id); }
    public int crear(Usuario e,char[] clave) throws SQLException { return biblioteca.crearUsuario(sesion,e,clave); }
    public void actualizar(Usuario e,char[] clave) throws SQLException { biblioteca.actualizarUsuario(sesion,e,clave); }
}
