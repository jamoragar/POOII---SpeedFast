package vista;
import controlador.ControladorAcceso;
import controlador.ControladorAplicacion;
import modelo.Sesion;
import util.Mensajes;
import util.Validaciones;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class VentanaAcceso extends JFrame {
    private JPanel panel;
    private JTextField txtRut;
    private JPasswordField txtClave;
    private JButton btnIngresar,btnSalir;
    private boolean ocupado;
    public VentanaAcceso(ControladorAplicacion app) {
        super("Biblioteca Escolar — Ingreso");setContentPane(panel);setSize(530,230);setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){@Override public void windowClosing(WindowEvent e){if(!ocupado)app.salir();}});
        btnSalir.addActionListener(e->{if(!ocupado)app.salir();});
        btnIngresar.addActionListener(e->{
            if(ocupado)return;
            try {
                String rut=Validaciones.rut(txtRut.getText());char[] clave=txtClave.getPassword();
                if(clave.length==0)throw new IllegalArgumentException("Ingresa tu contraseña.");
                ocupado=true;btnIngresar.setEnabled(false);txtRut.setEnabled(false);txtClave.setEnabled(false);
                new SwingWorker<Sesion,Void>() {
                    @Override protected Sesion doInBackground() throws Exception {return new ControladorAcceso().ingresar(rut,clave);}
                    @Override protected void done() {
                        try{app.abrir(get());dispose();}catch(Exception ex){Mensajes.error(VentanaAcceso.this,ex);}
                        finally{java.util.Arrays.fill(clave,'\0');txtClave.setText("");ocupado=false;btnIngresar.setEnabled(true);txtRut.setEnabled(true);txtClave.setEnabled(true);}
                    }
                }.execute();
            } catch(IllegalArgumentException ex){Mensajes.error(this,ex);}
        });getRootPane().setDefaultButton(btnIngresar);
    }
}
