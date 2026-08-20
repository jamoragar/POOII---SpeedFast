package speedfast;

public class Main {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       SISTEMA SPEEDFAST        ");
        System.out.println("================================");

        // Sustitución: cada objeto derivado se almacena como Pedido.
        Pedido[] pedidos = {
            new PedidoComida(1, "Av. Italia 456", 4.0),
            new PedidoEncomienda(2, "Av. Independencia 123", 6.0,
                    8.0, 30.0, 25.0, 40.0, true),
            new PedidoExpress(3, "Av. Apoquindo 1500", 7.0)
        };

        System.out.println("\n--- TIEMPOS ESTIMADOS DE ENTREGA ---\n");

        for (Pedido pedido : pedidos) {
            pedido.mostrarResumen();
            System.out.println("Tiempo estimado de entrega: "
                    + pedido.calcularTiempoEntrega() + " minutos");
            System.out.println();
        }

        System.out.println("\n--- SOBRESCRITURA SIN PARÁMETROS ---\n");

        // La misma llamada ejecuta el método del tipo real de cada objeto.
        for (Pedido pedido : pedidos) {
            pedido.asignarRepartidor();
            System.out.println();
        }

        System.out.println("--- SOBRECARGA CON STRING ---\n");

        String[] nombres = {"Juan Pérez", "Camila Soto", "Luis Díaz"};

        for (int i = 0; i < pedidos.length; i++) {
            pedidos[i].asignarRepartidor(nombres[i]);
            System.out.println();
        }

        System.out.println("--- POLIMORFISMO CON REPARTIDOR ---\n");

        Repartidor[] candidatosAptos = {
            new Repartidor("Juan Pérez", true, true,
                    15.0, 60.0, 50.0, 60.0, 4.5),
            new Repartidor("Camila Soto", true, false,
                    20.0, 50.0, 45.0, 70.0, 3.0),
            new Repartidor("Luis Díaz", true, true,
                    10.0, 40.0, 40.0, 50.0, 2.4)
        };

        for (int i = 0; i < pedidos.length; i++) {
            pedidos[i].asignarRepartidor(candidatosAptos[i]);
            System.out.println();
        }

        System.out.println("--- VALIDACIONES RECHAZADAS ---\n");

        Repartidor sinMochila = new Repartidor("Pedro Rojas", true, false,
                12.0, 50.0, 50.0, 50.0, 1.0);
        pedidos[0].asignarRepartidor(sinMochila);
        System.out.println();

        Repartidor comidaNoDisponible = new Repartidor("Carlos Vega",
                false, true, 12.0, 50.0, 50.0, 50.0, 0.8);
        pedidos[0].asignarRepartidor(comidaNoDisponible);
        System.out.println();

        Repartidor sinCapacidad = new Repartidor("Ana Silva", true, true,
                5.0, 20.0, 20.0, 30.0, 2.0);
        pedidos[1].asignarRepartidor(sinCapacidad);
        System.out.println();

        Repartidor sinEspacio = new Repartidor("José Araya", true, true,
                20.0, 20.0, 20.0, 30.0, 2.5);
        pedidos[1].asignarRepartidor(sinEspacio);
        System.out.println();

        Pedido encomiendaMalEmbalada = new PedidoEncomienda(4,
                "Calle Central 654", 4.0,
                3.0, 20.0, 20.0, 20.0, false);
        encomiendaMalEmbalada.asignarRepartidor(candidatosAptos[1]);
        System.out.println();

        System.out.println("--- EXPRESS: SELECCIÓN POR CERCANÍA ---\n");

        // El más cercano está no disponible; debe elegir a Luis (1.7 km),
        // aunque no es el primer candidato del arreglo.
        Repartidor[] candidatosExpress = {
            new Repartidor("María Torres", true, true,
                    10.0, 40.0, 40.0, 50.0, 8.4),
            new Repartidor("Diego Muñoz", false, true,
                    10.0, 40.0, 40.0, 50.0, 0.5),
            new Repartidor("Luis Díaz", true, true,
                    10.0, 40.0, 40.0, 50.0, 1.7),
            new Repartidor("Sofía Castro", true, false,
                    10.0, 40.0, 40.0, 50.0, 3.2)
        };
        pedidos[2].asignarRepartidor(candidatosExpress);

        System.out.println("\n--- EXPRESS: SIN DISPONIBILIDAD ---\n");

        Repartidor[] candidatosNoDisponibles = {
            new Repartidor("Diego Muñoz", false, true,
                    10.0, 40.0, 40.0, 50.0, 0.5),
            new Repartidor("Laura Vidal", false, true,
                    10.0, 40.0, 40.0, 50.0, 1.2)
        };
        pedidos[2].asignarRepartidor(candidatosNoDisponibles);

        System.out.println("\n================================");
        System.out.println("        FIN DEL PROGRAMA         ");
        System.out.println("================================");
    }
}
