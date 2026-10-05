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

public class VentanaEntregas extends VentanaGestion<Entrega> {
    private JPanel panel;
    private JTable tabla;
    private JButton btnCrear, btnActualizar, btnEliminar, btnLimpiar, btnRefrescar;
    private JTextField txtId;
    private JComboBox<Pedido> cmbPedido;
    private JComboBox<Repartidor> cmbRepartidor;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JComboBox<Object> filtroPedido;
    private JComboBox<Object> filtroRepartidor;
    private final EntregaControlador controlador = new EntregaControlador();

    public VentanaEntregas(Runnable notificar) {
        super("Entregas", notificar);
        configurar(panel, tabla, new String[]{"ID", "Pedido", "Repartidor", "Fecha", "Hora"},
                btnCrear, btnActualizar, btnEliminar, btnLimpiar, btnRefrescar);
        filtroPedido.addActionListener(e -> { if (!cargandoCombos) aplicarFiltros(); });
        filtroRepartidor.addActionListener(e -> { if (!cargandoCombos) aplicarFiltros(); });
        limpiar();
    }
    @Override protected int id(Entrega e) { return e.getId(); }
    @Override protected Object[] fila(Entrega e) {
        String hora = e.getHora() == null ? "" : e.getHora().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return new Object[]{e.getId(), nombrePedido(e.getIdPedido()), nombreRepartidor(e.getIdRepartidor()), e.getFecha(), hora};
    }
    @Override protected Entrega leer(int id) { Pedido pedido = (Pedido) cmbPedido.getSelectedItem();
        Repartidor repartidor = (Repartidor) cmbRepartidor.getSelectedItem();
        if (pedido == null || repartidor == null) throw new IllegalArgumentException("Selecciona pedido y repartidor. Regístralos primero si las listas están vacías.");
        return new Entrega(id, pedido.getId(), repartidor.getId(), Validaciones.fecha(txtFecha.getText()), Validaciones.hora(txtHora.getText())); }
    @Override protected void mostrar(Entrega e) { txtId.setText("" + e.getId()); seleccionar(cmbPedido, e.getIdPedido()); seleccionar(cmbRepartidor, e.getIdRepartidor());
        txtFecha.setText(e.getFecha() == null ? "" : e.getFecha().toString());
        txtHora.setText(e.getHora() == null ? "" : e.getHora().format(DateTimeFormatter.ofPattern("HH:mm:ss"))); }
    @Override protected void limpiar() { txtId.setText(""); cmbPedido.setSelectedIndex(-1); cmbRepartidor.setSelectedIndex(-1);
        txtFecha.setText(LocalDate.now().toString()); txtHora.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))); }
    @Override protected List<Entrega> consultar() throws Exception { List<Pedido> nuevosPedidos = new PedidoControlador().readAll();
        List<Repartidor> nuevosRepartidores = new RepartidorControlador().readAll();
        List<Entrega> entregas = controlador.readAll();
        pedidos = nuevosPedidos; repartidores = nuevosRepartidores;
        return entregas; }
    @Override protected int create(Entrega e) throws Exception { return controlador.create(e); }
    @Override protected boolean update(Entrega e) throws Exception { return controlador.update(e); }
    @Override protected boolean delete(int id) throws Exception { return controlador.delete(id); }
    private List<Pedido> pedidos = List.of();
    private List<Repartidor> repartidores = List.of();
    private boolean cargandoCombos;
    private String nombrePedido(int id) { return pedidos.stream().filter(p -> p.getId() == id).map(Object::toString).findFirst().orElse("" + id); }
    private String nombreRepartidor(int id) { return repartidores.stream().filter(p -> p.getId() == id).map(Object::toString).findFirst().orElse("" + id); }
    private int identidad(Object valor) {
        if (valor instanceof Pedido p) return p.getId();
        if (valor instanceof Repartidor r) return r.getId();
        return -1;
    }
    private void seleccionar(JComboBox<?> combo, int id) {
        combo.setSelectedIndex(-1);
        for (int i = 0; i < combo.getItemCount(); i++) if (identidad(combo.getItemAt(i)) == id) {
            combo.setSelectedIndex(i); break;
        }
    }
    /** Reconstruye las listas conservando la identidad y la edición del formulario. */
    @Override protected void actualizarRelaciones() {
        int p = identidad(cmbPedido.getSelectedItem()), r = identidad(cmbRepartidor.getSelectedItem());
        int fp = identidad(filtroPedido.getSelectedItem()), fr = identidad(filtroRepartidor.getSelectedItem());
        cargandoCombos = true;
        try {
            cmbPedido.setModel(new DefaultComboBoxModel<>(pedidos.toArray(Pedido[]::new)));
            cmbRepartidor.setModel(new DefaultComboBoxModel<>(repartidores.toArray(Repartidor[]::new)));
            seleccionar(cmbPedido, p); seleccionar(cmbRepartidor, r);
            filtroPedido.removeAllItems(); filtroPedido.addItem("Todos"); pedidos.forEach(filtroPedido::addItem);
            filtroRepartidor.removeAllItems(); filtroRepartidor.addItem("Todos"); repartidores.forEach(filtroRepartidor::addItem);
            seleccionar(filtroPedido, fp); seleccionar(filtroRepartidor, fr);
            if (filtroPedido.getSelectedIndex() < 0) filtroPedido.setSelectedIndex(0);
            if (filtroRepartidor.getSelectedIndex() < 0) filtroRepartidor.setSelectedIndex(0);
        } finally { cargandoCombos = false; }
    }
    @Override protected void aplicarFiltros() {
        filtrar(new RowFilter<DefaultTableModel, Integer>() {
            @Override public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> e) {
                return (filtroPedido.getSelectedIndex() <= 0 || e.getStringValue(1).equals(filtroPedido.getSelectedItem().toString()))
                    && (filtroRepartidor.getSelectedIndex() <= 0 || e.getStringValue(2).equals(filtroRepartidor.getSelectedItem().toString()));
            }
        });
    }
}
