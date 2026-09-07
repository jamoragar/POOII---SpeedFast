/**
 * Notificación especializada para dispositivos registrados.
 */
public class NotificacionPush extends Notificacion {

    public NotificacionPush(int id, String mensaje) {
        super(id, mensaje);
    }

    @Override
    public void enviar() {
        System.out.println("[PUSH]");
        System.out.println("Buscando dispositivo registrado...");
        System.out.println("Enviando notificación al dispositivo...");
        System.out.println("Mensaje: " + getMensaje());
        System.out.println("Notificación enviada correctamente.");
    }

    @Override
    public void enviar(String destinatario) {
        System.out.println("[PUSH]");
        System.out.println("Dispositivo: " + destinatario);
        System.out.println("Buscando dispositivo registrado...");
        System.out.println("Mensaje: " + getMensaje());
        System.out.println("Notificación Push enviada correctamente.");
    }
}