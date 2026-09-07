/**
 * Notificación especializada para mensajes SMS.
 */
public class NotificacionSMS extends Notificacion {

    public NotificacionSMS(int id, String mensaje) {
        super(id, mensaje);
    }

    @Override
    public void enviar() {
        System.out.println("[SMS]");
        System.out.println("Verificando número telefónico...");
        System.out.println("Enviando mensaje SMS...");
        System.out.println("Mensaje: " + getMensaje());
        System.out.println("Mensaje enviado correctamente.");
    }

    @Override
    public void enviar(String destinatario) {
        System.out.println("[SMS]");
        System.out.println("Número telefónico: " + destinatario);
        System.out.println("Verificando número telefónico...");
        System.out.println("Mensaje: " + getMensaje());
        System.out.println("SMS enviado correctamente.");
    }
}
