package controlador;
import modelo.*;
import servicio.BibliotecaServicio;
import java.sql.SQLException;
import java.util.List;
public final class ControladorReportes {
    private final Sesion sesion;
    public ControladorReportes(Sesion sesion) { this.sesion=sesion; }
    public boolean esBibliotecario() { return sesion.usuario().puedeRealizar(Accion.ADMINISTRAR); }
    public List<Estudiante> estudiantes() throws SQLException { return BibliotecaServicio.getInstance().estudiantes(sesion); }
    public Reporte consultar(String tipo,int id) throws SQLException { return BibliotecaServicio.getInstance().reporte(sesion,tipo,id); }
}
