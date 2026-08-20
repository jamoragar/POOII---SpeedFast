package speedfast;

/**
 * Clase abstracta con los datos y comportamientos comunes de un pedido.
 */
public abstract class Pedido {

    private int idPedido;
    private String direccionEntrega;
    private double distanciaKm;

    public Pedido(int idPedido, String direccionEntrega, double distanciaKm) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.distanciaKm = distanciaKm;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    /**
     * Muestra la información común y los factores propios del tipo de pedido.
     */
    public void mostrarResumen() {
        System.out.printf("%s #%03d%n", getTipoEntrega(), idPedido);
        System.out.println("Dirección: " + direccionEntrega);
        System.out.println("Distancia: " + formatearDistancia() + " km");
        System.out.println("Factores: " + getFactoresDuracion());
    }

    public abstract int calcularTiempoEntrega();

    protected abstract String getTipoEntrega();

    protected abstract String getFactoresDuracion();

    private String formatearDistancia() {
        if (distanciaKm == Math.rint(distanciaKm)) {
            return String.valueOf((int) distanciaKm);
        }

        return String.valueOf(distanciaKm);
    }

    /**
     * Método base que será sobrescrito por las clases hijas.
     */
    public void asignarRepartidor() {
        System.out.println("[PEDIDO GENÉRICO]");
        System.out.println("Asignando repartidor para pedido de tipo: "
                + getTipoEntrega());
    }

    /**
     * Sobrecarga del método asignarRepartidor().
     * Esta versión recibe el nombre del repartidor.
     */
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("[PEDIDO GENÉRICO]");
        System.out.println("Asignando repartidor para pedido de tipo: "
                + getTipoEntrega());
        System.out.println("-> ASIGNADO a " + nombreRepartidor);
    }

    /**
     * Sobrecarga que recibe los datos de un repartidor candidato.
     */
    public void asignarRepartidor(Repartidor repartidor) {
        System.out.println("[PEDIDO GENÉRICO]");

        if (repartidor == null) {
            System.out.println("-> NO ASIGNADO: el repartidor no es válido.");
            return;
        }

        if (!repartidor.isDisponible()) {
            System.out.println("-> NO ASIGNADO: " + repartidor.getNombre()
                    + " no tiene disponibilidad inmediata.");
            return;
        }

        System.out.println("-> ASIGNADO a " + repartidor.getNombre());
    }

    /**
     * Sobrecarga que permite evaluar varios repartidores candidatos.
     */
    public void asignarRepartidor(Repartidor[] repartidores) {
        System.out.println("[PEDIDO GENÉRICO]");

        if (repartidores == null || repartidores.length == 0) {
            System.out.println("-> NO ASIGNADO: no hay repartidores candidatos.");
            return;
        }

        for (Repartidor repartidor : repartidores) {
            if (repartidor != null && repartidor.isDisponible()) {
                asignarRepartidor(repartidor);
                return;
            }
        }

        System.out.println("-> NO ASIGNADO: no hay disponibilidad inmediata.");
    }
}
