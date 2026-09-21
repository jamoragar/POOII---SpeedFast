package vista;

import controlador.ControladorPedidos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaPrincipal extends JFrame {
    // Componentes vinculados a VentanaPrincipal.form por IntelliJ.
    private JPanel panelPrincipal;
    private JButton btnRegistrar;
    private JButton btnListar;
    private JButton btnAsignar;

    private final ControladorPedidos controlador = new ControladorPedidos();
    private VentanaRegistroPedido ventanaRegistro;
    private VentanaListaPedidos ventanaLista;

    public VentanaPrincipal() {
        super("SpeedFast | Gestión de entregas");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        btnRegistrar.addActionListener(e -> abrirRegistro());
        btnListar.addActionListener(e -> abrirListado(false));
        btnAsignar.addActionListener(e -> abrirListado(true));
        controlador.agregarObservadorError(mensaje -> JOptionPane.showMessageDialog(this,
                mensaje, "Error de entrega", JOptionPane.ERROR_MESSAGE));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evento) { cerrarAplicacion(); }
        });
        setContentPane(panelPrincipal);
        setSize(520, 330);
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private void abrirRegistro() {
        if (ventanaRegistro == null || !ventanaRegistro.isDisplayable()) {
            ventanaRegistro = new VentanaRegistroPedido(controlador);
            ventanaRegistro.setLocationRelativeTo(this);
        }
        mostrarVentana(ventanaRegistro);
    }

    private void abrirListado(boolean asignar) {
        if (ventanaLista == null || !ventanaLista.isDisplayable()) {
            ventanaLista = new VentanaListaPedidos(controlador);
            ventanaLista.setLocationRelativeTo(this);
        }
        mostrarVentana(ventanaLista);
        if (asignar) {
            ventanaLista.enfocarAsignacion();
        }
    }

    private void mostrarVentana(JFrame ventana) {
        ventana.setVisible(true);
        ventana.setState(Frame.NORMAL);
        ventana.toFront();
    }

    private void cerrarAplicacion() {
        if (controlador.hayEntregasActivas()) {
            int respuesta = JOptionPane.showConfirmDialog(this,
                    "Hay entregas en curso. ¿Deseas cancelarlas y salir?",
                    "Cerrar SpeedFast", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }
        }
        controlador.cerrar();
        if (ventanaRegistro != null) { ventanaRegistro.dispose(); }
        if (ventanaLista != null) { ventanaLista.dispose(); }
        dispose();
    }
}
