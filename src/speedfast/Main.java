package speedfast;

public class Main {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       SISTEMA SPEEDFAST        ");
        System.out.println("================================");

        // Objetos específicos de cada clase hija.
        PedidoComida comida =
                new PedidoComida(1, "Av. Siempre Viva 123");

        PedidoEncomienda encomienda =
                new PedidoEncomienda(2, "Calle Los Alerces 456");

        PedidoExpress express =
                new PedidoExpress(3, "Pasaje El Molino 789");

        System.out.println("\n--- PRUEBA DE SOBRESCRITURA ---\n");

        comida.asignarRepartidor();

        System.out.println("\n-------------------------------\n");

        encomienda.asignarRepartidor();

        System.out.println("\n-------------------------------\n");

        express.asignarRepartidor();

        System.out.println("\n--- PRUEBA DE SOBRECARGA ---\n");

        comida.asignarRepartidor("Juan Pérez");

        System.out.println();

        encomienda.asignarRepartidor("Camila Soto");

        System.out.println();

        express.asignarRepartidor("Luis Díaz");

        System.out.println("\n--- PRUEBA DE POLIMORFISMO ---\n");

        // La referencia es de tipo Pedido,
        // pero el objeto real pertenece a una clase hija.
        Pedido pedido1 =
                new PedidoComida(4, "Av. Los Pinos 321");

        Pedido pedido2 =
                new PedidoEncomienda(5, "Calle Central 654");

        Pedido pedido3 =
                new PedidoExpress(6, "Av. El Bosque 987");

        pedido1.asignarRepartidor();
        System.out.println();

        pedido2.asignarRepartidor();
        System.out.println();

        pedido3.asignarRepartidor();

        System.out.println("\n================================");
        System.out.println("        FIN DEL PROGRAMA         ");
        System.out.println("================================");
    }
}
