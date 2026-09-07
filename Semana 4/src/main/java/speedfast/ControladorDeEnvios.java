package speedfast;

import java.util.ArrayList;

/**
 * Coordina las reservas y mantiene el historial general de entregas.
 */
public class ControladorDeEnvios implements Rastreable {

    private final ArrayList<Pedido> historialEntregas = new ArrayList<>();

    public void reservarPedido(Pedido pedido) {
        if (pedido == null) {
            System.out.println("-> RESERVA RECHAZADA: el pedido no es válido.");
            return;
        }

        pedido.reservar();
    }

    public synchronized void registrarEntrega(Pedido pedido) {
        if (pedido == null) {
            System.out.println("-> REGISTRO RECHAZADO: el pedido no es válido.");
            return;
        }

        System.out.println("Registrando entrega de " + pedido.getDescripcion() + "...");

        if (!pedido.isDespachado()) {
            System.out.println("-> REGISTRO RECHAZADO: el pedido no fue despachado.");
            return;
        }

        if (contienePedido(pedido.getIdPedido())) {
            System.out.println("-> La entrega ya se encuentra registrada.");
            return;
        }

        if (!pedido.marcarEntregado()) {
            System.out.println("-> REGISTRO RECHAZADO: el estado del pedido "
                    + "no permite registrar la entrega.");
            return;
        }

        historialEntregas.add(pedido);
        System.out.println("-> Entrega registrada correctamente.");
    }

    @Override
    public synchronized void verHistorial() {
        System.out.println("Historial de entregas:");

        if (historialEntregas.isEmpty()) {
            System.out.println("- Sin entregas registradas.");
            return;
        }

        for (Pedido pedido : historialEntregas) {
            System.out.println("- " + pedido.getDescripcion()
                    + " - entregado por " + pedido.getNombreRepartidor());
        }
    }

    private boolean contienePedido(int idPedido) {
        for (Pedido pedido : historialEntregas) {
            if (pedido.getIdPedido() == idPedido) {
                return true;
            }
        }

        return false;
    }
}
