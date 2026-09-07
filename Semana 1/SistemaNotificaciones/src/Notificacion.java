/**
 * Clase base del sistema de notificaciones.
 * Representa una notificación genérica.
 */
public class Notificacion {

    private int id;
    private String mensaje;

    public Notificacion(int id, String mensaje) {
        this.id = id;
        this.mensaje = mensaje;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Método base que será sobrescrito por las clases hijas.
     */
    public void enviar() {
        System.out.println("Enviando notificación genérica...");
        System.out.println("Mensaje: " + mensaje);
    }

    /**
     * Sobrecarga del método enviar().
     * Esta versión recibe un destinatario.
     */
    public void enviar(String destinatario) {
        System.out.println("Enviando notificación a: " + destinatario);
        System.out.println("Mensaje: " + mensaje);
    }
}
