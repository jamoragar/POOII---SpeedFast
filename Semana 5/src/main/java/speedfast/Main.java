package speedfast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws java.io.UnsupportedEncodingException{
        // Para no tener problemas con tildes ni con Ñ
        System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        System.out.println("=== SPEEDFAST: ZONA DE CARGA ===\n");

        ZonaDeCarga zonaDeCarga = new ZonaDeCarga();
        List<Pedido> pedidos = crearPedidos(zonaDeCarga);

        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Future<?>> tareas = new ArrayList<>();

        tareas.add(executor.submit(new Repartidor("Juan", zonaDeCarga)));
        tareas.add(executor.submit(new Repartidor("Camila", zonaDeCarga)));
        tareas.add(executor.submit(new Repartidor("Pedro", zonaDeCarga)));
        executor.shutdown();

        boolean tareasFinalizadas = esperarFinalizacion(executor, tareas);

        System.out.println("\n--- RESUMEN FINAL ---");
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
        }

        if (tareasFinalizadas && todosEntregados(pedidos)) {
            System.out.println("\nTodos los pedidos han sido entregados correctamente.");
        } else {
            System.out.println("\nNo todos los pedidos pudieron ser entregados.");
        }
    }

    private static List<Pedido> crearPedidos(ZonaDeCarga zonaDeCarga) {
        List<Pedido> pedidos = List.of(
                new Pedido(1, "Santiago Centro"),
                new Pedido(2, "Providencia"),
                new Pedido(3, "Ñuñoa"),
                new Pedido(4, "Recoleta"),
                new Pedido(5, "Las Condes"),
                new Pedido(6, "La Florida")
        );

        System.out.println("[Zona de carga inicializada]\n");
        for (Pedido pedido : pedidos) {
            zonaDeCarga.agregarPedido(pedido);
        }

        System.out.println();
        return pedidos;
    }

    private static boolean esperarFinalizacion(ExecutorService executor,
            List<Future<?>> tareas) {
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                System.out.println("[MAIN] Se agotó el tiempo de espera.");
                executor.shutdownNow();
                return executor.awaitTermination(5, TimeUnit.SECONDS);
            }

            for (Future<?> tarea : tareas) {
                tarea.get();
            }

            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
            return false;
        } catch (ExecutionException e) {
            System.out.println("[MAIN] Una entrega terminó con error: "
                    + e.getCause().getMessage());
            executor.shutdownNow();
            return false;
        }
    }

    private static boolean todosEntregados(List<Pedido> pedidos) {
        for (Pedido pedido : pedidos) {
            if (pedido.getEstado() != EstadoPedido.ENTREGADO) {
                return false;
            }
        }

        return true;
    }
}
