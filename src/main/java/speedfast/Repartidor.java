package speedfast;

/**
 * Representa un repartidor candidato para atender un pedido.
 */
public class Repartidor {

    private String nombre;
    private boolean disponible;
    private boolean tieneMochilaTermica;
    private double capacidadPesoKg;
    private double altoMaximoCm;
    private double anchoMaximoCm;
    private double largoMaximoCm;
    private double distanciaAlPedidoKm;

    public Repartidor(String nombre, boolean disponible,
            boolean tieneMochilaTermica, double capacidadPesoKg,
            double altoMaximoCm, double anchoMaximoCm,
            double largoMaximoCm, double distanciaAlPedidoKm) {
        this.nombre = nombre;
        this.disponible = disponible;
        this.tieneMochilaTermica = tieneMochilaTermica;
        this.capacidadPesoKg = capacidadPesoKg;
        this.altoMaximoCm = altoMaximoCm;
        this.anchoMaximoCm = anchoMaximoCm;
        this.largoMaximoCm = largoMaximoCm;
        this.distanciaAlPedidoKm = distanciaAlPedidoKm;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public boolean isTieneMochilaTermica() {
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
}
