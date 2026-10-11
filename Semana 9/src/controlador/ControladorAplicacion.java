package controlador;
import modelo.*;
import util.DatabaseConnection;
import vista.*;
import javax.swing.*;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

/** Coordina navegación y refresco entre vistas, sin incluir consultas SQL. */
public final class ControladorAplicacion {
    private Sesion sesion;
    private VentanaAcceso acceso;
    private VentanaPrincipal principal;
    private VentanaLibros libros;
    private VentanaEstudiantes estudiantes;
    private VentanaUsuarios usuarios;
    private VentanaCategorias categorias;
    private VentanaPrestamos prestamos;
    private VentanaReportes reportes;
    private final List<Actualizable> ventanas=new ArrayList<>();
    public void iniciar(){acceso=new VentanaAcceso(this);acceso.setVisible(true);}
    public void abrir(Sesion sesion){this.sesion=sesion;principal=new VentanaPrincipal(this,sesion);principal.setVisible(true);}
    private void mostrar(JFrame frame,Actualizable ventana){if(!ventanas.contains(ventana))ventanas.add(ventana);frame.setVisible(true);frame.toFront();ventana.refrescar();}
    private void admin(){if(sesion==null || !sesion.usuario().puedeRealizar(Accion.ADMINISTRAR))throw new SecurityException("Acceso restringido.");}
    public void libros(){if(libros==null)libros=new VentanaLibros(new ControladorLibros(sesion),this::notificar);mostrar(libros,libros);}
    public void estudiantes(){admin();if(estudiantes==null)estudiantes=new VentanaEstudiantes(new ControladorEstudiantes(sesion),this::notificar);mostrar(estudiantes,estudiantes);}
    public void usuarios(){admin();if(usuarios==null)usuarios=new VentanaUsuarios(new ControladorUsuarios(sesion),this::notificar);mostrar(usuarios,usuarios);}
    public void categorias(){admin();if(categorias==null)categorias=new VentanaCategorias(new ControladorCategorias(sesion),this::notificar);mostrar(categorias,categorias);}
    public void prestamos(){if(prestamos==null)prestamos=new VentanaPrestamos(new ControladorPrestamos(sesion),this::notificar);mostrar(prestamos,prestamos);}
    public void reportes(){if(reportes==null)reportes=new VentanaReportes(new ControladorReportes(sesion));mostrar(reportes,reportes);}
    private void notificar(){ventanas.forEach(Actualizable::refrescar);}
    private boolean ocupado(){if(ventanas.stream().anyMatch(Actualizable::estaOcupado)){JOptionPane.showMessageDialog(principal,"Espera a que termine la operación en curso.");return true;}return false;}
    public void cerrarSesion(){if(ocupado())return;cerrarVentanas();sesion=null;iniciar();}
    public void salir(){if(ocupado())return;cerrarVentanas();new Thread(()->DatabaseConnection.getInstance().cerrar(),"Cerrar conexión JDBC").start();}
    private void cerrarVentanas(){
        for(Actualizable v:ventanas)((JFrame)v).dispose();ventanas.clear();
        if(principal!=null)principal.dispose();if(acceso!=null)acceso.dispose();
        libros=null;estudiantes=null;usuarios=null;categorias=null;prestamos=null;reportes=null;
    }
}
