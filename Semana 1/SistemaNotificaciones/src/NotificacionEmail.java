/**
 * Notificación especializada para correo electrónico.
 */
public class NotificacionEmail extends Notificacion {

    public NotificacionEmail(int id, String mensaje) {
        super(id, mensaje);
    }

    /**
     * Sobrescritura del método enviar().
     */
    @Override
    public void enviar() {
        System.out.println("[EMAIL]");
        System.out.println("Verificando dirección de correo...");
        System.out.println("Enviando correo electrónico...");
        System.out.println("Mensaje: " + getMensaje());
        System.out.println("Mensaje enviado correctamente.");
    }

    /**
     * Esta clase personaliza además la versión sobrecargada.
     */
    @Override
    public void enviar(String destinatario) {
        System.out.println("[EMAIL]");
        System.out.println("Destinatario: " + destinatario);
        System.out.println("Verificando dirección de correo...");
        System.out.println("Mensaje: " + getMensaje());
        System.out.println("Correo enviado correctamente.");
    }
}
