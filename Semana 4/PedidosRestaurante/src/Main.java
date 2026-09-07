//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {

        System.out.println("=== SIMULADOR CONCURRENTE DE PEDIDOS ===\n");

        GestorPedidos gestor = new GestorPedidos();

        int pedidosPorCamarero = 4;
        int totalPedidos = pedidosPorCamarero * 3;

        // Cada objeto Camarero y Cocina implementa Runnable.
        Camarero camarero1 = new Camarero("Camila", gestor, pedidosPorCamarero);
        Camarero camarero2 = new Camarero("Leo", gestor, pedidosPorCamarero);
        Camarero camarero3 = new Camarero("Sofía", gestor, pedidosPorCamarero);
        Cocina cocina = new Cocina(gestor, totalPedidos);

        // Convertimos cada tarea en un hilo.
        Thread hiloCamarero1 = new Thread(camarero1);
        Thread hiloCamarero2 = new Thread(camarero2);
        Thread hiloCamarero3 = new Thread(camarero3);
        Thread hiloCocina = new Thread(cocina);

        // start() inicia los hilos. Desde aquí trabajan concurrentemente.
        hiloCamarero1.start();
        hiloCamarero2.start();
        hiloCamarero3.start();
        hiloCocina.start();

        try {
            // join() hace que Main espere a que los demás hilos terminen.
            hiloCamarero1.join();
            hiloCamarero2.join();
            hiloCamarero3.join();
            hiloCocina.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== SIMULACIÓN FINALIZADA ===");
    }
}