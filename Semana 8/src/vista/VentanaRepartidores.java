package vista;
import controlador.*;
import modelo.*;
import util.Validaciones;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class VentanaRepartidores extends VentanaGestion<Repartidor> {
    private JPanel panel;
    private JTable tabla;
    private JButton btnCrear, btnActualizar, btnEliminar, btnLimpiar, btnRefrescar;
    private JTextField txtId;
    private JTextField txtNombre;
    private final RepartidorControlador controlador = new RepartidorControlador();

    public VentanaRepartidores(Runnable notificar) {
        super("Repartidores", notificar);
        configurar(panel, tabla, new String[]{"ID", "Nombre"},
                btnCrear, btnActualizar, btnEliminar, btnLimpiar, btnRefrescar);
        
        limpiar();
    }
    @Override protected int id(Repartidor e) { return e.getId(); }
    @Override protected Object[] fila(Repartidor e) { return new Object[]{e.getId(), e.getNombre()}; }
    @Override protected Repartidor leer(int id) { return new Repartidor(id, Validaciones.texto(txtNombre.getText(), "Nombre")); }
    @Override protected void mostrar(Repartidor e) { txtId.setText("" + e.getId()); txtNombre.setText(e.getNombre()); }
    @Override protected void limpiar() { txtId.setText(""); txtNombre.setText(""); }
    @Override protected List<Repartidor> consultar() throws Exception { return controlador.readAll(); }
    @Override protected int create(Repartidor e) throws Exception { return controlador.create(e); }
    @Override protected boolean update(Repartidor e) throws Exception { return controlador.update(e); }
    @Override protected boolean delete(int id) throws Exception { return controlador.delete(id); }
    
}
