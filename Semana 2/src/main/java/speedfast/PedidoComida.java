package speedfast;

/**
 * Pedido especializado para restaurantes.
 */
public class PedidoComida extends Pedido {

    private static final String TIPO_ENTREGA = "PedidoComida";
    private static final int TIEMPO_BASE_MINUTOS = 15;
    private static final double MINUTOS_POR_KM = 2.0;

    public PedidoComida(int idPedido, String direccionEntrega,
            double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    @Override
    public int calcularTiempoEntrega() {
        double tiempo = TIEMPO_BASE_MINUTOS
                + MINUTOS_POR_KM * getDistanciaKm();
        return (int) Math.round(tiempo);
    }

    @Override
    protected String getTipoEntrega() {
        return TIPO_ENTREGA;
    }

    @Override
    protected String getFactoresDuracion() {
        return TIEMPO_BASE_MINUTOS + " min base + "
                + (int) MINUTOS_POR_KM + " min por kilómetro";
    }

    /**
     * Sobrescritura del método asignarRepartidor().
     */
    @Override
    public void asignarRepartidor() {
        System.out.println("[PEDIDO COMIDA]");
        System.out.println("Criterio: disponibilidad inmediata y mochila térmica.");
    }

    /**
     * Versión sobrecargada con validación para comida.
     */
    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("[PEDIDO COMIDA]");
        System.out.println("-> Verificando mochila térmica... OK");
        System.out.println("-> ASIGNADO a " + nombreRepartidor);
    }

    /**
     * Sobrescritura con validaciones reales del repartidor.
     */
    @Override
    public void asignarRepartidor(Repartidor repartidor) {
        System.out.println("[PEDIDO COMIDA]");

        if (repartidor == null) {
            System.out.println("-> NO ASIGNADO: el repartidor no es válido.");
            return;
        }

        System.out.println("Evaluando a " + repartidor.getNombre() + "...");

        if (!repartidor.isDisponible()) {
            System.out.println("-> NO ASIGNADO: no tiene disponibilidad inmediata.");
            return;
        }

        if (!repartidor.isTieneMochilaTermica()) {
            System.out.println("-> NO ASIGNADO: no tiene mochila térmica.");
            return;
        }

        System.out.println("-> Mochila térmica y disponibilidad verificadas.");
        System.out.println("-> ASIGNADO a " + repartidor.getNombre());
    }

    @Override
    public void asignarRepartidor(Repartidor[] repartidores) {
        if (repartidores != null) {
            for (Repartidor repartidor : repartidores) {
                if (repartidor != null && repartidor.isDisponible()
                        && repartidor.isTieneMochilaTermica()) {
                    asignarRepartidor(repartidor);
                    return;
                }
            }
        }

        System.out.println("[PEDIDO COMIDA]");
        System.out.println("-> NO ASIGNADO: no hay candidatos disponibles "
                + "con mochila térmica.");
    }
}
