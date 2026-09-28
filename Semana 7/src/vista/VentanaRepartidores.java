package vista;

import controlador.ControladorPedidos;
import modelo.Repartidor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaRepartidores extends JFrame {

    private final ControladorPedidos controlador;
    private JPanel panelPrincipal;
    private JTextField txtNombre;
    private JButton btnGuardar;
    private JButton btnRefrescar;
    private JTable tabla;
    private boolean cargando;
    private boolean recargar;
    private boolean cerrada;
    private final Runnable observador = this::cargarRepartidores;
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Disponible"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) { return false; }
    };

    public VentanaRepartidores(ControladorPedidos controlador) {
        super("SpeedFast | Repartidores");
        this.controlador = controlador;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        tabla.setModel(modeloTabla);
        btnGuardar.addActionListener(e -> guardarRepartidor());
        btnRefrescar.addActionListener(e -> cargarRepartidores());
        setContentPane(panelPrincipal);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
        controlador.agregarObservador(observador);
        cargarRepartidores();
    }

    private void guardarRepartidor() {
        String nombre = txtNombre.getText();
        try { new Repartidor(nombre); }
        catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos no válidos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        btnGuardar.setEnabled(false);
        txtNombre.setEditable(false);
        controlador.ejecutarEnSegundoPlano(() -> controlador.registrarRepartidor(nombre), id -> {
            txtNombre.setText("");
            controlador.notificarCambios();
            JOptionPane.showMessageDialog(this, "Repartidor #" + id + " guardado en MySQL.");
        }, () -> {
            btnGuardar.setEnabled(true);
            txtNombre.setEditable(true);
        });
    }

    private void cargarRepartidores() {
        if (cerrada) { return; }
        if (cargando) { recargar = true; return; }
        cargando = true;
        btnRefrescar.setEnabled(false);
        controlador.ejecutarEnSegundoPlano(controlador::obtenerRepartidores, repartidores -> {
            if (cerrada) { return; }
            modeloTabla.setRowCount(0);
            for (Repartidor repartidor : repartidores) {
                modeloTabla.addRow(new Object[]{repartidor.getId(), repartidor.getNombre(),
                        repartidor.isDisponible() ? "Sí" : "No"});
            }
        }, () -> {
            cargando = false;
            btnRefrescar.setEnabled(true);
            if (recargar && !cerrada) { recargar = false; cargarRepartidores(); }
        });
    }

    @Override
    public void dispose() {
        cerrada = true;
        controlador.quitarObservador(observador);
        super.dispose();
    }
}
