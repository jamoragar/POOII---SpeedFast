package vista;
import controlador.ControladorUsuarios;
import modelo.*;
import util.Validaciones;
import javax.swing.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.time.LocalDate;

public final class VentanaUsuarios extends VentanaGestion<Usuario> {
    private JPanel panel;
    private JTable tabla;
    private JButton btnCrear, btnActualizar, btnEliminar, btnLimpiar, btnRefrescar;
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtRut;
    private JTextField txtCorreo;
    private JComboBox<String> cmbRol;
    private JPasswordField txtClave;
    private final ControladorUsuarios gestion;
    public VentanaUsuarios(ControladorUsuarios gestion,Runnable notificar) {
        super("Usuarios",gestion,notificar);this.gestion=gestion;
        configurar(panel,tabla,new String[]{"ID", "Nombre", "RUT", "Correo", "Rol"},btnCrear,btnActualizar,btnEliminar,btnLimpiar,btnRefrescar);
        cmbRol.setModel(new DefaultComboBoxModel<>(new String[]{"estudiante","bibliotecario"}));
        limpiar();
    }
    @Override protected Usuario leer(int id) { if(cmbRol.getSelectedItem()==null)throw new IllegalArgumentException("Selecciona un rol.");
        return "bibliotecario".equals(cmbRol.getSelectedItem())?new Bibliotecario(id,Validaciones.texto(txtNombre.getText(),"Nombre",100),Validaciones.rut(txtRut.getText()),Validaciones.correo(txtCorreo.getText()),null):new UsuarioEstudiante(id,Validaciones.texto(txtNombre.getText(),"Nombre",100),Validaciones.rut(txtRut.getText()),Validaciones.correo(txtCorreo.getText()),null,-1); }
    @Override protected Object[] fila(Usuario e) { return new Object[]{e.getId(),e.getNombre(),e.getRut(),e.getCorreo(),e.getRol()}; }
    @Override protected void mostrar(Usuario e) {txtId.setText(""+e.getId());txtNombre.setText(e.getNombre());txtRut.setText(e.getRut());txtCorreo.setText(e.getCorreo());txtClave.setText("");cmbRol.setSelectedItem(e.getRol());}
    @Override protected void limpiar() {txtId.setText("");txtNombre.setText("");txtRut.setText("");txtCorreo.setText("");txtClave.setText("");cmbRol.setSelectedIndex(0);}
    @Override protected List<Usuario> consultar() throws Exception { return controlador.listar(); }
    @Override protected Callable<Integer> prepararCreacion(Usuario dato) {
        char[] clave=txtClave.getPassword();Validaciones.clave(clave);
        return ()->{try {return gestion.crear(dato,clave);}finally {java.util.Arrays.fill(clave,'\0');}};
    }
    @Override protected Callable<Integer> prepararActualizacion(Usuario original,Usuario dato) {
        char[] clave=txtClave.getPassword();if(clave.length>0)Validaciones.clave(clave);
        return ()->{try {gestion.actualizar(dato,clave);return dato.getId();}finally {java.util.Arrays.fill(clave,'\0');}};
    }
    
}
