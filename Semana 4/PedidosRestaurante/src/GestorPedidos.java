import java.util.concurrent.PriorityBlockingQueue;

/**
 * Recurso compartido entre los camareros y la cocina.
 */
public class GestorPedidos {

    private final PriorityBlockingQueue<Pedido> colaPedidos = new PriorityBlockingQueue<>();

    public void agregarPedido(Pedido pedido) {
        colaPedidos.put(pedido);
    }

    /**
     * take() espera si la cola está vacía.
     */
    public Pedido tomarPedido() throws InterruptedException {
        return colaPedidos.take();
    }
}
