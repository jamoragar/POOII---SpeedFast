package speedfast;

/**
 * Pedido especializado para supermercado o farmacia.
 */
public class PedidoExpress extends Pedido {

    private static final String TIPO_ENTREGA = "PedidoExpress";
    private static final int TIEMPO_BASE_MINUTOS = 10;
    private static final double LIMITE_DISTANCIA_KM = 5.0;
    private static final int RECARGO_MINUTOS = 5;

    public PedidoExpress(int idPedido, String direccionEntrega,
            double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    @Override
    public int calcularTiempoEntrega() {
        if (getDistanciaKm() > LIMITE_DISTANCIA_KM) {
            return TIEMPO_BASE_MINUTOS + RECARGO_MINUTOS;
        }

        return TIEMPO_BASE_MINUTOS;
    }

    @Override
    protected String getTipoEntrega() {
        return TIPO_ENTREGA;
    }

    @Override
    protected String getFactoresDuracion() {
        return TIEMPO_BASE_MINUTOS + " min base + "
                + RECARGO_MINUTOS + " min si supera "
                + (int) LIMITE_DISTANCIA_KM + " km";
    }

    /**
     * Sobrescritura del método asignarRepartidor().
     */
    @Override
    public void asignarRepartidor() {
        System.out.println("[PEDIDO EXPRESS]");
        System.out.println("Criterio: menor distancia con disponibilidad inmediata.");
    }

    /**
     * Versión sobrecargada con validación para compras express.
     */
    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("[PEDIDO EXPRESS]");
        System.out.println("-> Verificando cercanía y disponibilidad... OK");
        System.out.println("-> ASIGNADO a " + nombreRepartidor);
    }

    @Override
    public void asignarRepartidor(Repartidor repartidor) {
        System.out.println("[PEDIDO EXPRESS]");

        if (repartidor == null) {
            System.out.println("-> NO ASIGNADO: el repartidor no es válido.");
            return;
        }

        if (!repartidor.isDisponible()) {
            System.out.println("-> NO ASIGNADO: " + repartidor.getNombre()
                    + " no tiene disponibilidad inmediata.");
            return;
        }

        System.out.println("-> Distancia evaluada: "
                + repartidor.getDistanciaAlPedidoKm() + " km.");
        System.out.println("-> ASIGNADO a " + repartidor.getNombre());
    }

    /**
     * Selecciona efectivamente al candidato disponible con menor distancia.
     */
    @Override
    public void asignarRepartidor(Repartidor[] repartidores) {
        System.out.println("[PEDIDO EXPRESS]");

        if (repartidores == null || repartidores.length == 0) {
            System.out.println("-> NO ASIGNADO: no hay repartidores candidatos.");
            return;
        }

        Repartidor masCercano = null;

        for (Repartidor repartidor : repartidores) {
            if (repartidor == null) {
                continue;
            }

            System.out.println("-> Candidato " + repartidor.getNombre()
                    + ": " + repartidor.getDistanciaAlPedidoKm() + " km, "
                    + (repartidor.isDisponible() ? "disponible" : "no disponible"));

            if (repartidor.isDisponible() && (masCercano == null
                    || repartidor.getDistanciaAlPedidoKm()
                    < masCercano.getDistanciaAlPedidoKm())) {
                masCercano = repartidor;
            }
        }

        if (masCercano == null) {
            System.out.println("-> NO ASIGNADO: no hay disponibilidad inmediata.");
            return;
        }

        System.out.println("-> Menor distancia disponible: "
                + masCercano.getDistanciaAlPedidoKm() + " km.");
        System.out.println("-> ASIGNADO a " + masCercano.getNombre());
    }
}
