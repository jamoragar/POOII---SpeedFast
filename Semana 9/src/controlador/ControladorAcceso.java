package controlador;
import modelo.Sesion;
import servicio.SeguridadServicio;
import java.sql.SQLException;
public final class ControladorAcceso {
    public Sesion ingresar(String rut,char[] clave) throws SQLException { return new SeguridadServicio().ingresar(rut,clave); }
}
