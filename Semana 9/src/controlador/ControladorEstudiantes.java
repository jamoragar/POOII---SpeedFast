package controlador;
import modelo.*;
import java.sql.SQLException;
import java.util.List;
public final class ControladorEstudiantes extends ControladorCRUD<Estudiante> {
    public ControladorEstudiantes(Sesion sesion) { super(sesion); }
    @Override public List<Estudiante> listar() throws SQLException { return biblioteca.estudiantes(sesion); }
    @Override public int crear(Estudiante e) throws SQLException { throw new IllegalArgumentException("Debes proporcionar la contraseña inicial."); }
    @Override public void actualizar(Estudiante original,Estudiante e) throws SQLException { biblioteca.actualizarEstudiante(sesion,e,new char[0]); }
    @Override public void eliminar(int id) throws SQLException { biblioteca.eliminarEstudiante(sesion,id); }
    public int crear(Estudiante e,char[] clave) throws SQLException { return biblioteca.crearEstudiante(sesion,e,clave); }
    public void actualizar(Estudiante e,char[] clave) throws SQLException { biblioteca.actualizarEstudiante(sesion,e,clave); }
}
