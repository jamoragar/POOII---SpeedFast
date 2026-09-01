package speedfast;

public class Main {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("          SISTEMA SPEEDFAST");
        System.out.println("========================================");

        ControladorDeEnvios controlador = new ControladorDeEnvios();

        // Sustitución: cada objeto derivado se almacena como Pedido.
        Pedido[] pedidos = {
            new PedidoComida(101, "Av. Italia 456", 4.0),
            new PedidoEncomienda(102, "Av. Santa Rosa 567", 7.0,
                    8.0, 30.0, 25.0, 40.0, true),
            new PedidoExpress(103, "Av. Apoquindo 1500", 7.0)
        };

        // Las interfaces permiten operar sin depender del tipo concreto.
        Despachable[] despachables = pedidos;
        Cancelable[] cancelables = pedidos;
        Rastreable rastreable = controlador;

        Repartidor[] candidatosComida = {
            new Repartidor("Pedro Rojas", true, false,
                    12.0, 50.0, 50.0, 50.0, 1.0),
            new Repartidor("Luis Díaz", true, true,
                    15.0, 60.0, 50.0, 60.0, 2.4)
        };

        System.out.println("\n--- CRITERIOS POLIMÓRFICOS DE ASIGNACIÓN ---\n");

        for (Pedido pedido : pedidos) {
            pedido.asignarRepartidor();
            System.out.println();
        }

        System.out.println("--- RESERVAS ---\n");

        for (Pedido pedido : pedidos) {
            controlador.reservarPedido(pedido);
            System.out.println();
        }

        System.out.println("--- ASIGNACIÓN AUTOMÁTICA Y MANUAL ---\n");

        pedidos[0].asignarRepartidor(candidatosComida);
        System.out.println();
        pedidos[1].asignarRepartidor("Daniela Tapia");

        System.out.println("\n--- RESUMEN Y TIEMPOS ESTIMADOS ---\n");

        for (Pedido pedido : pedidos) {
            pedido.mostrarResumen();
            System.out.println("Tiempo estimado: "
                    + pedido.calcularTiempoEntrega() + " minutos");
            System.out.println();
        }

        System.out.println("--- DESPACHOS Y ENTREGAS ---\n");

        despachables[0].despachar();
        controlador.registrarEntrega(pedidos[0]);
        System.out.println();

        despachables[1].despachar();
        controlador.registrarEntrega(pedidos[1]);

        System.out.println("\n--- CANCELACIÓN Y VALIDACIONES ---\n");

        // Un pedido reservado sin repartidor no puede despacharse.
        despachables[2].despachar();
        cancelables[2].cancelar();

        // Un pedido cancelado tampoco puede despacharse ni registrarse.
        despachables[2].despachar();
        controlador.registrarEntrega(pedidos[2]);

        // Una entrega ya registrada no puede duplicarse.
        controlador.registrarEntrega(pedidos[0]);

        // Otro tipo concreto también usa el contrato Cancelable.
        cancelables[0].cancelar();

        System.out.println("\n--- HISTORIAL FINAL ---\n");
        rastreable.verHistorial();

        System.out.println("\n========================================");
        System.out.println("             FIN DEL PROGRAMA");
        System.out.println("========================================");
    }
}
