package speedfast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Representa un repartidor candidato y una tarea de entrega concurrente.
 */
public class Repartidor implements Runnable {

    private final String nombre;
    private final boolean disponible;
    private final boolean tieneMochilaTermica;
    private final double capacidadPesoKg;
    private final double altoMaximoCm;
    private final double anchoMaximoCm;
    private final double largoMaximoCm;
    private final double distanciaAlPedidoKm;
    private final List<Pedido> pedidosAsignados = new ArrayList<>();
    private final Random random = new Random();
    private final ControladorDeEnvios controlador;

    public Repartidor(String nombre, boolean disponible,
            boolean tieneMochilaTermica, double capacidadPesoKg,
            double altoMaximoCm, double anchoMaximoCm,
            double largoMaximoCm, double distanciaAlPedidoKm) {
        this(nombre, disponible, tieneMochilaTermica, capacidadPesoKg,
                altoMaximoCm, anchoMaximoCm, largoMaximoCm,
                distanciaAlPedidoKm, null);
    }

    public Repartidor(String nombre, boolean disponible,
            boolean tieneMochilaTermica, double capacidadPesoKg,
            double altoMaximoCm, double anchoMaximoCm,
            double largoMaximoCm, double distanciaAlPedidoKm,
            ControladorDeEnvios controlador) {
        this.nombre = nombre;
        this.disponible = disponible;
        this.tieneMochilaTermica = tieneMochilaTermica;
        this.capacidadPesoKg = capacidadPesoKg;
        this.altoMaximoCm = altoMaximoCm;
        this.anchoMaximoCm = anchoMaximoCm;
        this.largoMaximoCm = largoMaximoCm;
        this.distanciaAlPedidoKm = distanciaAlPedidoKm;
        this.controlador = controlador;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean tieneNombreValido() {
        return nombre != null && !nombre.isBlank();
    }

    public boolean isDisponible() {
        return disponible;
    }

    public boolean tieneMochilaTermica() {
        return tieneMochilaTermica;
    }

    public double getCapacidadPesoKg() {
        return capacidadPesoKg;
    }

    public double getAltoMaximoCm() {
        return altoMaximoCm;
    }

    public double getAnchoMaximoCm() {
        return anchoMaximoCm;
    }

    public double getLargoMaximoCm() {
        return largoMaximoCm;
    }

    public double getDistanciaAlPedidoKm() {
        return distanciaAlPedidoKm;
    }

    public void agregarPedido(Pedido pedido) {
        if (pedido == null) {
            System.out.println("[Repartidor " + nombre + "] no puede recibir un pedido nulo.");
            return;
        }

        if (pedidosAsignados.contains(pedido)) {
            System.out.println("[Repartidor " + nombre + "] ya tiene asignado "
                    + pedido.getDescripcion() + ".");
            return;
        }

        pedidosAsignados.add(pedido);
    }

    @Override
    public void run() {
        if (controlador == null) {
            System.out.println("[Repartidor " + nombre + "] no tiene controlador de envíos.");
            return;
        }

        for (Pedido pedido : pedidosAsignados) {
            System.out.println("[Repartidor " + nombre + "] inicia -> "
                    + pedido.getDescripcion());
            pedido.despachar();

            if (!pedido.isDespachado()) {
                System.out.println("[Repartidor " + nombre + "] no puede entregar -> "
                        + pedido.getDescripcion());
                continue;
            }

            try {
                int tiempoEntrega = 500 + random.nextInt(1000);
                System.out.println("[Repartidor " + nombre + "] en ruta -> "
                        + pedido.getDescripcion() + " (" + tiempoEntrega + " ms)");
                Thread.sleep(tiempoEntrega);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[Repartidor " + nombre + "] fue interrumpido.");
                return;
            }

            controlador.registrarEntrega(pedido);
            System.out.println("[Repartidor " + nombre + "] finaliza -> "
                    + pedido.getDescripcion());
        }

        System.out.println("[Repartidor " + nombre + "] terminó sus entregas.");
    }
}
