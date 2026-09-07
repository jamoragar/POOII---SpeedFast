import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Un camarero es una tarea que puede ejecutarse en su propio hilo.
 * Su trabajo es generar pedidos y enviarlos al GestorPedidos.
 */
public class Camarero implements Runnable {

    // Contador compartido y seguro para generar IDs únicos.
    private static final AtomicInteger CONTADOR_PEDIDOS = new AtomicInteger(1);

    private final String nombre;
    private final GestorPedidos gestor;
    private final int cantidadPedidos;
    private final Random random = new Random();

    private final String[] platos = {
            "Pizza",
            "Sopa",
            "Ensalada",
            "Lasaña",
            "Postre"
    };

    public Camarero(String nombre, GestorPedidos gestor, int cantidadPedidos) {
        this.nombre = nombre;
        this.gestor = gestor;
        this.cantidadPedidos = cantidadPedidos;
    }

    @Override
    public void run() {
        for (int i = 0; i < cantidadPedidos; i++) {
            int id = CONTADOR_PEDIDOS.getAndIncrement();
            String plato = platos[random.nextInt(platos.length)];
            PrioridadPedido prioridad = generarPrioridadAleatoria();

            Pedido pedido = new Pedido(id, plato, prioridad);

            System.out.println("[Camarero " + nombre + "] genera -> " + pedido);
            gestor.agregarPedido(pedido);

            try {
                // Simula el tiempo que tarda el camarero en tomar otro pedido.
                Thread.sleep(500 + random.nextInt(700));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[Camarero " + nombre + "] fue interrumpido.");
                return;
            }
        }

        System.out.println("[Camarero " + nombre + "] terminó su trabajo.");
    }

    private PrioridadPedido generarPrioridadAleatoria() {
        int valor = random.nextInt(3);

        return switch (valor) {
            case 0 -> PrioridadPedido.ALTA;
            case 1 -> PrioridadPedido.MEDIA;
            default -> PrioridadPedido.BAJA;
        };
    }
}
