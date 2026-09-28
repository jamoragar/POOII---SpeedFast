package vista;

import controlador.ControladorPedidos;
import modelo.Pedido;
import modelo.TipoPedido;
import javax.swing.*;

public class VentanaRegistroPedido extends JFrame {

    private final ControladorPedidos controlador;
    private JPanel panelPrincipal;
    private JTextField txtId;
    private JTextField txtDireccion;
    private JComboBox<TipoPedido> cmbTipo;
    private JButton btnGuardar;
    private JButton btnLimpiar;

    public VentanaRegistroPedido(ControladorPedidos controlador) {
        super("SpeedFast | Registrar pedido");
        this.controlador = controlador;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cmbTipo.setModel(new DefaultComboBoxModel<>(TipoPedido.values()));
        txtId.setEditable(false);
        txtId.setText("Automático (MySQL)");
        btnGuardar.addActionListener(e -> guardarPedido());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        getRootPane().setDefaultButton(btnGuardar);
        setContentPane(panelPrincipal);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private void guardarPedido() {
        String direccion = txtDireccion.getText();
        TipoPedido tipo = (TipoPedido) cmbTipo.getSelectedItem();
        try {
            new Pedido(direccion, tipo);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos no válidos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        btnGuardar.setEnabled(false);
        btnLimpiar.setEnabled(false);
        txtDireccion.setEditable(false);
        cmbTipo.setEnabled(false);
        controlador.ejecutarEnSegundoPlano(() -> controlador.registrarPedido(direccion, tipo), id -> {
            limpiarCampos();
            txtId.setText(String.valueOf(id));
            controlador.notificarCambios();
            JOptionPane.showMessageDialog(this, "Pedido #" + id + " guardado en MySQL.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
        }, () -> {
            btnGuardar.setEnabled(true);
            btnLimpiar.setEnabled(true);
            txtDireccion.setEditable(true);
            cmbTipo.setEnabled(true);
        });
    }

    private void limpiarCampos() {
        txtId.setText("Automático (MySQL)");
        txtDireccion.setText("");
        cmbTipo.setSelectedIndex(0);
        txtDireccion.requestFocusInWindow();
    }
}
