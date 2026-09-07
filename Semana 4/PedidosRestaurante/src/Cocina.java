/**
 * La cocina consume los pedidos que dejan los camareros.
 * También se ejecuta como una tarea independiente.
 */
public class Cocina implements Runnable {

    private final GestorPedidos gestor;
    private final int totalPedidosAProcesar;

    public Cocina(GestorPedidos gestor, int totalPedidosAProcesar) {
        this.gestor = gestor;
        this.totalPedidosAProcesar = totalPedidosAProcesar;
    }

    @Override
    public void run() {
        for (int i = 0; i < totalPedidosAProcesar; i++) {
            try {
                Pedido pedido = gestor.tomarPedido();

                System.out.println("    [Cocina] preparando -> " + pedido);

                // Simula el tiempo de preparación del plato.
                Thread.sleep(1000);

                System.out.println("    [Cocina] listo       -> Pedido #" + pedido.getId());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[Cocina] fue interrumpida.");
                return;
            }
        }

        System.out.println("[Cocina] terminó de procesar todos los pedidos.");
    }
}
