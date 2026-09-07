package speedfast;

/**
 * Pedido especializado para documentos o paquetes.
 */
public class PedidoEncomienda extends Pedido {

    private static final String TIPO_ENTREGA = "PedidoEncomienda";
    private static final int TIEMPO_BASE_MINUTOS = 20;
    private static final double MINUTOS_POR_KM = 1.5;

    private double pesoKg;
    private double altoCm;
    private double anchoCm;
    private double largoCm;
    private boolean embalajeAdecuado;

    public PedidoEncomienda(int idPedido, String direccionEntrega,
            double distanciaKm,
            double pesoKg, double altoCm, double anchoCm,
            double largoCm, boolean embalajeAdecuado) {
        super(idPedido, direccionEntrega, distanciaKm);
        this.pesoKg = pesoKg;
        this.altoCm = altoCm;
        this.anchoCm = anchoCm;
        this.largoCm = largoCm;
        this.embalajeAdecuado = embalajeAdecuado;
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
                + MINUTOS_POR_KM + " min por kilómetro";
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public double getAltoCm() {
        return altoCm;
    }

    public double getAnchoCm() {
        return anchoCm;
    }

    public double getLargoCm() {
        return largoCm;
    }

    public boolean isEmbalajeAdecuado() {
        return embalajeAdecuado;
    }

    /**
     * Sobrescritura del método asignarRepartidor().
     */
    @Override
    public void asignarRepartidor() {
        System.out.println("[PEDIDO ENCOMIENDA]");
        System.out.println("Criterio: disponibilidad, embalaje, peso y dimensiones.");
    }

    /**
     * Versión sobrecargada con validación para encomiendas.
     */
    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("[PEDIDO ENCOMIENDA]");
        System.out.println("-> Validando peso, dimensiones y embalaje... OK");
        System.out.println("-> ASIGNADO a " + nombreRepartidor);
    }

    /**
     * Sobrescritura con validaciones reales de carga y embalaje.
     */
    @Override
    public void asignarRepartidor(Repartidor repartidor) {
        System.out.println("[PEDIDO ENCOMIENDA]");

        if (!embalajeAdecuado) {
            System.out.println("-> NO ASIGNADO: el embalaje no es adecuado.");
            return;
        }

        if (repartidor == null) {
            System.out.println("-> NO ASIGNADO: el repartidor no es válido.");
            return;
        }

        System.out.println("Evaluando a " + repartidor.getNombre() + "...");

        if (!repartidor.isDisponible()) {
            System.out.println("-> NO ASIGNADO: no tiene disponibilidad inmediata.");
            return;
        }

        if (pesoKg > repartidor.getCapacidadPesoKg()) {
            System.out.println("-> NO ASIGNADO: el peso de " + pesoKg
                    + " kg supera su capacidad de "
                    + repartidor.getCapacidadPesoKg() + " kg.");
            return;
        }

        if (!soportaDimensiones(repartidor)) {
            System.out.println("-> NO ASIGNADO: las dimensiones superan "
                    + "la capacidad del repartidor.");
            return;
        }

        System.out.println("-> Embalaje, peso y dimensiones validados.");
        System.out.println("-> ASIGNADO a " + repartidor.getNombre());
    }

    @Override
    public void asignarRepartidor(Repartidor[] repartidores) {
        if (!embalajeAdecuado) {
            System.out.println("[PEDIDO ENCOMIENDA]");
            System.out.println("-> NO ASIGNADO: el embalaje no es adecuado.");
            return;
        }

        if (repartidores != null) {
            for (Repartidor repartidor : repartidores) {
                if (repartidor != null && repartidor.isDisponible()
                        && pesoKg <= repartidor.getCapacidadPesoKg()
                        && soportaDimensiones(repartidor)) {
                    asignarRepartidor(repartidor);
                    return;
                }
            }
        }

        System.out.println("[PEDIDO ENCOMIENDA]");
        System.out.println("-> NO ASIGNADO: ningún candidato disponible "
                + "soporta la encomienda.");
    }

    private boolean soportaDimensiones(Repartidor repartidor) {
        return altoCm <= repartidor.getAltoMaximoCm()
                && anchoCm <= repartidor.getAnchoMaximoCm()
                && largoCm <= repartidor.getLargoMaximoCm();
    }
}
