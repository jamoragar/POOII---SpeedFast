package vista;

import controlador.ControladorPedidos;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VentanaListaPedidos extends JFrame {
    private final ControladorPedidos controlador;
    private final ModeloPedidos modeloTabla = new ModeloPedidos();

    private static class ModeloPedidos extends DefaultTableModel {
        ModeloPedidos() {
            super(new String[]{"ID", "Dirección", "Tipo", "Estado", "Repartidor"}, 0);
        }

        @Override
        public boolean isCellEditable(int fila, int columna) { return false; }

        @Override
        public Class<?> getColumnClass(int columna) {
            return columna == 0 ? Integer.class : String.class;
        }

        void reemplazarPedidos(List<Pedido> pedidos) {
            Object[][] filas = new Object[pedidos.size()][5];
            for (int i = 0; i < pedidos.size(); i++) {
                Pedido pedido = pedidos.get(i);
                filas[i] = new Object[]{pedido.getId(), pedido.getDireccionEntrega(),
                        pedido.getTipo().toString(), pedido.getEstado().name(),
                        pedido.getRepartidor() == null ? "Sin asignar" : pedido.getRepartidor().getNombre()};
            }
            // Un solo evento de datos: no vaciar y agregar filas una por una.
            // Las columnas no cambian, por lo que se conserva su ordenación y ancho.
            dataVector.clear();
            dataVector.addAll(convertToVector(filas));
            fireTableDataChanged();
        }
    }
    // Componentes creados desde VentanaListaPedidos.form.
    private JPanel panelPrincipal;
    private JTable tabla;
    private JComboBox<Repartidor> cmbRepartidor;
    private JButton btnIniciar;
    private JButton btnRefrescar;
    private JLabel lblResumen;
    private final Runnable observador = this::actualizarTabla;
    private List<Pedido> pedidos = List.of();
    private boolean actualizandoTabla;
    private boolean cargando;
    private boolean recargar;
    private boolean cerrada;
    private boolean guardando;

    private record Datos(List<Pedido> pedidos, List<Repartidor> repartidores) { }


    public VentanaListaPedidos(ControladorPedidos controlador) {
        super("SpeedFast | Pedidos y entregas");
        this.controlador = controlador;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        tabla.setModel(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(280);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(130);
        cmbRepartidor.setPrototypeDisplayValue(new Repartidor("Seleccionar repartidor"));

        btnRefrescar.addActionListener(e -> actualizarTabla());
        btnIniciar.addActionListener(e -> iniciarEntrega());
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!actualizandoTabla && !e.getValueIsAdjusting()) {
                // Consultar los índices después de que JTable termine de notificar al sorter.
                SwingUtilities.invokeLater(this::actualizarBoton);
            }
        });
        cmbRepartidor.addActionListener(e -> actualizarBoton());
        setContentPane(panelPrincipal);
        controlador.agregarObservador(observador);
        actualizarTabla();
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private Pedido obtenerSeleccionado() {
        if (actualizandoTabla) {
            return null;
        }
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= tabla.getRowCount()) {
            return null;
        }
        int indice = tabla.convertRowIndexToModel(fila);
        return indice >= 0 && indice < pedidos.size() ? pedidos.get(indice) : null;
    }

    private void actualizarTabla() {
        if (cerrada) { return; }
        if (cargando) { recargar = true; return; }
        cargando = true;
        btnRefrescar.setEnabled(false);
        lblResumen.setText("Consultando MySQL...");
        actualizarBoton();
        controlador.ejecutarEnSegundoPlano(() -> new Datos(controlador.obtenerPedidos(),
                controlador.obtenerRepartidores()), datos -> {
            if (!cerrada) { mostrarDatos(datos); }
        }, () -> {
            cargando = false;
            btnRefrescar.setEnabled(true);
            if (lblResumen.getText().equals("Consultando MySQL...")) {
                lblResumen.setText("No se pudo actualizar. Los datos visibles pueden estar desactualizados.");
            }
            actualizarBoton();
            if (recargar && !cerrada) { recargar = false; actualizarTabla(); }
        });
    }

    private void mostrarDatos(Datos datos) {
        Pedido seleccionado = obtenerSeleccionado();
        Repartidor repartidorSeleccionado = (Repartidor) cmbRepartidor.getSelectedItem();
        actualizandoTabla = true;
        try {
            tabla.clearSelection();
            pedidos = datos.pedidos();
            modeloTabla.reemplazarPedidos(pedidos);
            if (seleccionado != null) {
                int indice = -1;
                for (int i = 0; i < pedidos.size(); i++) {
                    if (pedidos.get(i).getId() == seleccionado.getId()) { indice = i; break; }
                }
                if (indice >= 0) {
                    int fila = tabla.convertRowIndexToView(indice);
                    if (fila >= 0) {
                        tabla.setRowSelectionInterval(fila, fila);
                    }
                }
            }
            List<Repartidor> disponibles = datos.repartidores().stream().filter(Repartidor::isDisponible).toList();
            cmbRepartidor.setModel(new DefaultComboBoxModel<>(disponibles.toArray(new Repartidor[0])));
            if (repartidorSeleccionado != null) {
                for (Repartidor disponible : disponibles) {
                    if (disponible.getId() == repartidorSeleccionado.getId()) {
                        cmbRepartidor.setSelectedItem(disponible);
                        break;
                    }
                }
            }
            lblResumen.setText(pedidos.size() + " pedidos  |  " + disponibles.size()
                    + " repartidores disponibles");
        } finally {
            actualizandoTabla = false;
        }
        actualizarBoton();
    }

    private void actualizarBoton() {
        if (actualizandoTabla) { return; }
        Pedido pedido = obtenerSeleccionado();
        boolean enReparto = pedido != null && pedido.getEstado() == EstadoPedido.EN_REPARTO;
        btnIniciar.setText(enReparto ? "Finalizar entrega" : "Iniciar entrega");
        btnIniciar.setEnabled(!guardando && !cargando && pedido != null
                && !controlador.estaSimulando(pedido.getId())
                && (enReparto || (pedido.getEstado() == EstadoPedido.PENDIENTE
                && cmbRepartidor.getSelectedItem() != null)));
    }

    private void iniciarEntrega() {
        Pedido pedido = obtenerSeleccionado();
        if (pedido == null) { return; }
        guardando = true;
        actualizarBoton();
        Runnable terminado = () -> { guardando = false; actualizarBoton(); };
        try {
            if (pedido.getEstado() == EstadoPedido.EN_REPARTO) {
                controlador.finalizarEntrega(pedido, terminado);
            } else {
                controlador.iniciarEntrega(pedido, (Repartidor) cmbRepartidor.getSelectedItem(), terminado);
                // Otros pedidos pueden iniciarse mientras la simulación trabaja en segundo plano.
                guardando = false;
                actualizarBoton();
            }
        } catch (IllegalArgumentException ex) {
            terminado.run();
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se puede iniciar la entrega", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void enfocarAsignacion() {
        if (tabla.getRowCount() > 0) {
            tabla.requestFocusInWindow();
        }
    }

    @Override
    public void dispose() {
        cerrada = true;
        controlador.quitarObservador(observador);
        super.dispose();
    }
}
