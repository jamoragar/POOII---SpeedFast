package util;
import java.awt.Component;
import java.sql.SQLException;
import javax.swing.JOptionPane;
public final class Mensajes {
    private Mensajes() { }
    public static void error(Component padre,Throwable error) {
        while(error.getCause()!=null && !(error instanceof SQLException) && !(error instanceof IllegalArgumentException) && !(error instanceof SecurityException)) error=error.getCause();
        String mensaje=error.getMessage();
        if(error instanceof SQLException sql) {
            mensaje=switch(sql.getErrorCode()) {
                case 1062 -> "Ya existe un registro con ese RUT o ISBN.";
                case 1451 -> "El registro tiene préstamos o libros asociados. Resuelve esas referencias antes de eliminarlo.";
                case 1452 -> "Una referencia ya no existe. Refresca y vuelve a seleccionar.";
                case 1045 -> "MySQL rechazó las credenciales. Revisa config/db.properties.";
                case 1049,1146,1054 -> "La base no está preparada. Ejecuta los tres scripts SQL en el orden indicado.";
                case 1205,1213 -> "Otra operación está modificando estos datos. Refresca y vuelve a intentarlo.";
                default -> sql.getSQLState()!=null && sql.getSQLState().startsWith("08")?
                    "No se pudo conectar con MySQL. El listado visible puede estar desactualizado.":"No se completó la operación: "+mensaje;
            };
        }
        JOptionPane.showMessageDialog(padre,mensaje,"Biblioteca — Error",JOptionPane.ERROR_MESSAGE);
    }
}
