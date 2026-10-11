package vista;
import controlador.ControladorEstudiantes;
import modelo.*;
import util.Validaciones;
import javax.swing.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.time.LocalDate;

public final class VentanaEstudiantes extends VentanaGestion<Estudiante> {
    private JPanel panel;
    private JTable tabla;
    private JButton btnCrear, btnActualizar, btnEliminar, btnLimpiar, btnRefrescar;
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtRut;
    private JTextField txtCorreo;
    private JTextField txtCurso;
    private JPasswordField txtClave;
    private final ControladorEstudiantes gestion;
    public VentanaEstudiantes(ControladorEstudiantes gestion,Runnable notificar) {
        super("Estudiantes",gestion,notificar);this.gestion=gestion;
        configurar(panel,tabla,new String[]{"ID", "Nombre", "RUT", "Correo", "Curso"},btnCrear,btnActualizar,btnEliminar,btnLimpiar,btnRefrescar);
        
        limpiar();
    }
    @Override protected Estudiante leer(int id) { return new Estudiante(id,Validaciones.texto(txtNombre.getText(),"Nombre",100),Validaciones.rut(txtRut.getText()),Validaciones.correo(txtCorreo.getText()),Validaciones.texto(txtCurso.getText(),"Curso",20)); }
    @Override protected Object[] fila(Estudiante e) { return new Object[]{e.getId(),e.getNombre(),e.getRut(),e.getCorreo(),e.getCurso()}; }
    @Override protected void mostrar(Estudiante e) {txtId.setText(""+e.getId());txtNombre.setText(e.getNombre());txtRut.setText(e.getRut());txtCorreo.setText(e.getCorreo());txtClave.setText("");txtCurso.setText(e.getCurso());}
    @Override protected void limpiar() {txtId.setText("");txtNombre.setText("");txtRut.setText("");txtCorreo.setText("");txtClave.setText("");txtCurso.setText("");}
    @Override protected List<Estudiante> consultar() throws Exception { return controlador.listar(); }
    @Override protected Callable<Integer> prepararCreacion(Estudiante dato) {
        char[] clave=txtClave.getPassword();Validaciones.clave(clave);
        return ()->{try {return gestion.crear(dato,clave);}finally {java.util.Arrays.fill(clave,'\0');}};
    }
    @Override protected Callable<Integer> prepararActualizacion(Estudiante original,Estudiante dato) {
        char[] clave=txtClave.getPassword();if(clave.length>0)Validaciones.clave(clave);
        return ()->{try {gestion.actualizar(dato,clave);return dato.getId();}finally {java.util.Arrays.fill(clave,'\0');}};
    }
    
}
