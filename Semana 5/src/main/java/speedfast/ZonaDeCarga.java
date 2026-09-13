package speedfast;

import java.util.ArrayDeque;
import java.util.Queue;

public class ZonaDeCarga {

    private final Queue<Pedido> pedidos = new ArrayDeque<>();

    public synchronized void agregarPedido(Pedido pedido) {
        if (pedido == null) {
            System.out.println("Pedido rechazado: no es válido.");
            return;
        }

        pedidos.offer(pedido);
        System.out.println("Pedido #" + pedido.getId() + " agregado. Destino: "
                + pedido.getDireccionEntrega());
    }

    public synchronized Pedido retirarPedido() {
        Pedido pedido = pedidos.poll();

        if (pedido != null) {
            pedido.setEstado(EstadoPedido.EN_REPARTO);
        }

        return pedido;
    }
}
