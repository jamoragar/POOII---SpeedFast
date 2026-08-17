package speedfast;

/**
 * Pedido especializado para supermercado o farmacia.
 */
public class PedidoExpress extends Pedido {

    public PedidoExpress(int idPedido, String direccionEntrega) {
        super(idPedido, direccionEntrega, "Compra Express");
    }

    /**
     * Sobrescritura del método asignarRepartidor().
     */
    @Override
    public void asignarRepartidor() {
        System.out.println("[PEDIDO EXPRESS]");
        System.out.println("Asignando repartidor...");
        System.out.println("-> Repartidor más cercano con disponibilidad inmediata encontrado.");
    }

    /**
     * Versión sobrecargada con validación para compras express.
     */
    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("[PEDIDO EXPRESS]");
        System.out.println("Asignando repartidor...");
        System.out.println("-> Repartidor más cercano con disponibilidad inmediata encontrado.");
        System.out.println("-> Pedido asignado a " + nombreRepartidor);
    }
}
