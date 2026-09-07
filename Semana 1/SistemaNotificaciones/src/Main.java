public class Main {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       SISTEMA CONECTAAPP       ");
        System.out.println("================================");

        // Objetos específicos de cada clase hija.
        NotificacionEmail email =
                new NotificacionEmail(1, "Bienvenido a ConectaApp");

        NotificacionSMS sms =
                new NotificacionSMS(2, "Tu código de seguridad es 4587");

        NotificacionPush push =
                new NotificacionPush(3, "Tienes una nueva promoción");

        System.out.println("\n--- PRUEBA DE SOBRESCRITURA ---\n");

        email.enviar();

        System.out.println("\n-------------------------------\n");

        sms.enviar();

        System.out.println("\n-------------------------------\n");

        push.enviar();

        System.out.println("\n--- PRUEBA DE SOBRECARGA ---\n");

        email.enviar("estudiante@correo.cl");

        System.out.println();

        sms.enviar("+56912345678");

        System.out.println();

        push.enviar("Dispositivo-001");

        System.out.println("\n--- PRUEBA DE POLIMORFISMO ---\n");

        // La referencia es del tipo Notificacion,
        // pero el objeto real pertenece a una clase hija.
        Notificacion notificacion1 =
                new NotificacionEmail(4, "Recuperación de contraseña");

        Notificacion notificacion2 =
                new NotificacionSMS(5, "Tu pedido está listo");

        Notificacion notificacion3 =
                new NotificacionPush(6, "Nueva actualización disponible");

        notificacion1.enviar();
        System.out.println();

        notificacion2.enviar();
        System.out.println();

        notificacion3.enviar();

        System.out.println("\n================================");
        System.out.println("        FIN DEL PROGRAMA         ");
        System.out.println("================================");
    }
}
