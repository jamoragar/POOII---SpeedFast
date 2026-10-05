package util;
import java.sql.SQLException;
import java.awt.Component;
import javax.swing.JOptionPane;

public final class Mensajes {
    private Mensajes() { }
    public static void error(Component padre, Throwable error) {
        while (error.getCause() != null && !(error instanceof SQLException)) error = error.getCause();
        String mensaje = error.getMessage();
        if (error instanceof SQLException sql) {
            if (sql.getErrorCode() == 1451) mensaje = "Existen entregas asociadas. Primero elimínalas o reasígnalas.";
            else if (sql.getErrorCode() == 1452) mensaje = "El pedido o repartidor ya no existe. Refresca y selecciona una relación válida.";
            else if (sql.getSQLState() != null && sql.getSQLState().startsWith("08"))
                mensaje = "No se pudo conectar con MySQL. Revisa el servidor y config/db.properties. Los datos visibles pueden estar desactualizados.";
            else mensaje = "No se completó la operación en MySQL: " + mensaje;
        }
        JOptionPane.showMessageDialog(padre, mensaje, "SpeedFast — Error", JOptionPane.ERROR_MESSAGE);
    }
}
