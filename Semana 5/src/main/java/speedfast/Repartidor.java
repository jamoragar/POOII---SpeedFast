package speedfast;

import java.util.concurrent.ThreadLocalRandom;

public class Repartidor implements Runnable {

    private final String nombre;
    private final ZonaDeCarga zonaDeCarga;

    public Repartidor(String nombre, ZonaDeCarga zonaDeCarga) {
        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
    }

    @Override
    public void run() {
        while (true) {
            Pedido pedido = zonaDeCarga.retirarPedido();

            if (pedido == null) {
                System.out.println("[Repartidor - " + nombre
                        + "] Zona de carga vacía.");
                return;
            }

            System.out.println("[Repartidor - " + nombre + "] Retirando pedido #"
                    + pedido.getId() + " para " + pedido.getDireccionEntrega());
            System.out.println("[Repartidor - " + nombre + "] Pedido #"
                    + pedido.getId() + " -> " + pedido.getEstado());

            try {
                System.out.println("[Repartidor - " + nombre + "] Entregando pedido #"
                        + pedido.getId() + "...");
                Thread.sleep(ThreadLocalRandom.current().nextInt(350, 801));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[Repartidor - " + nombre + "] Entrega interrumpida "
                        + "para pedido #" + pedido.getId() + ".");
                return;
            }

            pedido.setEstado(EstadoPedido.ENTREGADO);
            System.out.println("[Repartidor - " + nombre + "] Pedido #"
                    + pedido.getId() + " -> " + pedido.getEstado());
        }
    }
}
