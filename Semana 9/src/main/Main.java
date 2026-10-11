package main;
import controlador.ControladorAplicacion;
import util.DatabaseConnection;
import javax.swing.SwingUtilities;
public final class Main {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(()->DatabaseConnection.getInstance().cerrar()));
        SwingUtilities.invokeLater(()->new ControladorAplicacion().iniciar());
    }
}
