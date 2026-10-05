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

public class VentanaPedidos extends VentanaGestion<Pedido> {
    private JPanel panel;
    private JTable tabla;
    private JButton btnCrear, btnActualizar, btnEliminar, btnLimpiar, btnRefrescar;
    private JTextField txtId;
    private JTextField txtDireccion;
    private JComboBox<TipoPedido> cmbTipo;
    private JComboBox<EstadoPedido> cmbEstado;
    private JComboBox<Object> filtroTipo;
    private JComboBox<Object> filtroEstado;
    private final PedidoControlador controlador = new PedidoControlador();

    public VentanaPedidos(Runnable notificar) {
        super("Pedidos", notificar);
        configurar(panel, tabla, new String[]{"ID", "Dirección", "Tipo", "Estado"},
                btnCrear, btnActualizar, btnEliminar, btnLimpiar, btnRefrescar);
        cmbTipo.setModel(new DefaultComboBoxModel<>(TipoPedido.values()));
        cmbEstado.setModel(new DefaultComboBoxModel<>(EstadoPedido.values()));
        filtroTipo.addItem("Todos"); for (TipoPedido t : TipoPedido.values()) filtroTipo.addItem(t);
        filtroEstado.addItem("Todos"); for (EstadoPedido e : EstadoPedido.values()) filtroEstado.addItem(e);
        filtroTipo.addActionListener(e -> aplicarFiltros());
        filtroEstado.addActionListener(e -> aplicarFiltros());
        limpiar();
    }
    @Override protected int id(Pedido e) { return e.getId(); }
    @Override protected Object[] fila(Pedido e) { return new Object[]{e.getId(), e.getDireccion(), e.getTipo(), e.getEstado()}; }
    @Override protected Pedido leer(int id) { if (cmbTipo.getSelectedItem() == null || cmbEstado.getSelectedItem() == null)
            throw new IllegalArgumentException("Selecciona tipo y estado.");
        return new Pedido(id, Validaciones.texto(txtDireccion.getText(), "Dirección"),
                (TipoPedido) cmbTipo.getSelectedItem(), (EstadoPedido) cmbEstado.getSelectedItem()); }
    @Override protected void mostrar(Pedido e) { txtId.setText("" + e.getId()); txtDireccion.setText(e.getDireccion()); cmbTipo.setSelectedItem(e.getTipo()); cmbEstado.setSelectedItem(e.getEstado()); }
    @Override protected void limpiar() { txtId.setText(""); txtDireccion.setText(""); cmbTipo.setSelectedIndex(0); cmbEstado.setSelectedItem(EstadoPedido.PENDIENTE); }
    @Override protected List<Pedido> consultar() throws Exception { return controlador.readAll(); }
    @Override protected int create(Pedido e) throws Exception { return controlador.create(e); }
    @Override protected boolean update(Pedido e) throws Exception { return controlador.update(e); }
    @Override protected boolean delete(int id) throws Exception { return controlador.delete(id); }
    @Override protected void aplicarFiltros() {
        filtrar(new RowFilter<DefaultTableModel, Integer>() {
            @Override public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> e) {
                return (filtroTipo.getSelectedIndex() <= 0 || e.getValue(2) == filtroTipo.getSelectedItem())
                    && (filtroEstado.getSelectedIndex() <= 0 || e.getValue(3) == filtroEstado.getSelectedItem());
            }
        });
    }
}
