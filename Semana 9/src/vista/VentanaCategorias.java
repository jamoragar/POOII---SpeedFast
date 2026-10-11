package vista;
import controlador.ControladorCategorias;
import modelo.*;
import util.Validaciones;
import javax.swing.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.time.LocalDate;

public final class VentanaCategorias extends VentanaGestion<Categoria> {
    private JPanel panel;
    private JTable tabla;
    private JButton btnCrear, btnActualizar, btnEliminar, btnLimpiar, btnRefrescar;
    private JTextField txtId;
    private JTextField txtNombre;
    private final ControladorCategorias gestion;
    public VentanaCategorias(ControladorCategorias gestion,Runnable notificar) {
        super("Categorias",gestion,notificar);this.gestion=gestion;
        configurar(panel,tabla,new String[]{"ID", "Nombre"},btnCrear,btnActualizar,btnEliminar,btnLimpiar,btnRefrescar);
        
        limpiar();
    }
    @Override protected Categoria leer(int id) { return new Categoria(id,Validaciones.texto(txtNombre.getText(),"Nombre",100)); }
    @Override protected Object[] fila(Categoria e) { return new Object[]{e.getId(),e.getNombre()}; }
    @Override protected void mostrar(Categoria e) {txtId.setText(""+e.getId());txtNombre.setText(e.getNombre());}
    @Override protected void limpiar() {txtId.setText("");txtNombre.setText("");}
    @Override protected List<Categoria> consultar() throws Exception { return controlador.listar(); }
    
    
}
