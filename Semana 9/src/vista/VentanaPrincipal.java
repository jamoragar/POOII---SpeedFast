package vista;
import controlador.ControladorAplicacion;
import modelo.*;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
public final class VentanaPrincipal extends JFrame {
    private JPanel panel;
    private JLabel lblSesion;
    private JButton btnLibros,btnEstudiantes,btnUsuarios,btnCategorias,btnPrestamos,btnReportes,btnCerrarSesion;
    public VentanaPrincipal(ControladorAplicacion app,Sesion sesion) {
        super("Biblioteca Escolar");setContentPane(panel);setSize(1180,210);setLocationRelativeTo(null);
        lblSesion.setText(sesion.usuario().descripcion());
        boolean admin=sesion.usuario().puedeRealizar(Accion.ADMINISTRAR);
        btnEstudiantes.setVisible(admin);btnUsuarios.setVisible(admin);btnCategorias.setVisible(admin);
        btnLibros.addActionListener(e->app.libros());btnEstudiantes.addActionListener(e->app.estudiantes());
        btnUsuarios.addActionListener(e->app.usuarios());btnCategorias.addActionListener(e->app.categorias());
        btnPrestamos.addActionListener(e->app.prestamos());btnReportes.addActionListener(e->app.reportes());
        btnCerrarSesion.addActionListener(e->app.cerrarSesion());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){@Override public void windowClosing(WindowEvent e){app.salir();}});
    }
}
