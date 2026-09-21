package vista;

import controlador.ControladorPedidos;
import modelo.TipoPedido;

import javax.swing.*;

public class VentanaRegistroPedido extends JFrame {
    private final ControladorPedidos controlador;
    // Componentes creados desde VentanaRegistroPedido.form.
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
        btnGuardar.addActionListener(e -> guardarPedido());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        getRootPane().setDefaultButton(btnGuardar);
        setContentPane(panelPrincipal);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private void guardarPedido() {
        try {
            controlador.registrarPedido(txtId.getText(), txtDireccion.getText(),
                    (TipoPedido) cmbTipo.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Pedido registrado correctamente.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "El ID debe ser un número entero entre 1 y 2147483647.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            txtId.requestFocusInWindow();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Datos no válidos", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtDireccion.setText("");
        cmbTipo.setSelectedIndex(0);
        txtId.requestFocusInWindow();
    }
}
