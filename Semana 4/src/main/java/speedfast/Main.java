package speedfast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws java.io.UnsupportedEncodingException {
        System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        System.out.println("========================================");
        System.out.println("          SISTEMA SPEEDFAST");
        System.out.println("========================================");

        ControladorDeEnvios controlador = new ControladorDeEnvios();

        // Cada objeto derivado se almacena como Pedido.
        Pedido[] pedidos = {
            new PedidoComida(101, "Av. Italia 456", 4.0),
            new PedidoEncomienda(102, "Av. Santa Rosa 567", 7.0,
                    8.0, 30.0, 25.0, 40.0, true),
            new PedidoExpress(103, "Av. Apoquindo 1500", 7.0),
            new PedidoComida(104, "Av. Providencia 890", 3.5),
            new PedidoEncomienda(105, "Av. Vicuña Mackenna 1200", 5.0,
                    4.0, 20.0, 20.0, 30.0, true),
            new PedidoExpress(106, "Av. Las Condes 2250", 2.0)
        };

        Repartidor ana = new Repartidor("Ana", true, true,
                15.0, 60.0, 50.0, 60.0, 1.0, controlador);
        Repartidor luis = new Repartidor("Luis", true, true,
                20.0, 70.0, 60.0, 70.0, 2.0, controlador);
        Repartidor carla = new Repartidor("Carla", true, false,
                10.0, 45.0, 45.0, 45.0, 1.5, controlador);

        System.out.println("\n--- RESERVAS Y ASIGNACIONES ---\n");

        for (Pedido pedido : pedidos) {
            controlador.reservarPedido(pedido);
        }

        pedidos[0].asignarRepartidor(ana);
        pedidos[1].asignarRepartidor(luis);
        pedidos[2].asignarRepartidor(carla);
        pedidos[3].asignarRepartidor(ana);
        pedidos[4].asignarRepartidor(luis);
        pedidos[5].asignarRepartidor(carla);

        ana.agregarPedido(pedidos[0]);
        ana.agregarPedido(pedidos[3]);
        luis.agregarPedido(pedidos[1]);
        luis.agregarPedido(pedidos[4]);
        carla.agregarPedido(pedidos[2]);
        carla.agregarPedido(pedidos[5]);

        System.out.println("\n--- RESUMEN Y TIEMPOS ESTIMADOS ---\n");

        for (Pedido pedido : pedidos) {
            pedido.mostrarResumen();
            System.out.println("Tiempo estimado: "
                    + pedido.calcularTiempoEntrega() + " minutos");
            System.out.println();
        }

        System.out.println("--- ENTREGAS CONCURRENTES ---\n");

        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Future<?>> tareas = new ArrayList<>();
        tareas.add(executor.submit(ana));
        tareas.add(executor.submit(luis));
        tareas.add(executor.submit(carla));
        executor.shutdown();

        esperarTareas(tareas, executor);

        System.out.println("\n--- HISTORIAL FINAL ---\n");
        controlador.verHistorial();

        System.out.println("\n========================================");
        System.out.println("             FIN DEL PROGRAMA");
        System.out.println("========================================");
    }

    private static void esperarTareas(List<Future<?>> tareas,
            ExecutorService executor) {
        try {
            for (Future<?> tarea : tareas) {
                tarea.get();
            }

            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("El tiempo de espera terminó. Se cancelan las tareas pendientes.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("La simulación fue interrumpida.");
            executor.shutdownNow();
        } catch (ExecutionException e) {
            System.out.println("Una entrega terminó con error: " + e.getCause().getMessage());
            executor.shutdownNow();
        }
    }
}
