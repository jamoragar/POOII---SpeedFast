package vista;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private JPanel panel;
    private JButton btnPedidos, btnRepartidores, btnEntregas;
    private final List<VentanaGestion<?>> ventanas = new ArrayList<>();
    private VentanaPedidos pedidos;
    private VentanaRepartidores repartidores;
    private VentanaEntregas entregas;

    public VentanaPrincipal() {
        super("SpeedFast — Semana 8");
        setContentPane(panel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                if (ventanas.stream().anyMatch(VentanaGestion::estaOcupado)) {
                    JOptionPane.showMessageDialog(VentanaPrincipal.this, "Espera a que termine la operación en curso antes de salir.");
                } else {
                    ventanas.forEach(JFrame::dispose);
                    dispose();
                }
            }
        });
        setSize(520, 320); setLocationRelativeTo(null);
        btnPedidos.addActionListener(e -> {
            if (pedidos == null) { pedidos = new VentanaPedidos(this::notificar); ventanas.add(pedidos); }
            abrir(pedidos);
        });
        btnRepartidores.addActionListener(e -> {
            if (repartidores == null) { repartidores = new VentanaRepartidores(this::notificar); ventanas.add(repartidores); }
            abrir(repartidores);
        });
        btnEntregas.addActionListener(e -> {
            if (entregas == null) { entregas = new VentanaEntregas(this::notificar); ventanas.add(entregas); }
            abrir(entregas);
        });
    }
    private void abrir(VentanaGestion<?> ventana) { ventana.setVisible(true); ventana.toFront(); ventana.refrescar(); }
    private void notificar() { ventanas.forEach(VentanaGestion::refrescar); }
}
