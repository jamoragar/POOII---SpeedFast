package speedfast;

/**
 * Clase abstracta con los datos y comportamientos comunes de un pedido.
 */
public abstract class Pedido implements Despachable, Cancelable {

    private int idPedido;
    private String direccionEntrega;
    private double distanciaKm;
    private String nombreRepartidor;
    private boolean reservado;
    private boolean despachado;
    private boolean cancelado;
    private boolean entregado;

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

    public String getNombreRepartidor() {
        if (nombreRepartidor == null) {
            return "Sin asignar";
        }

        return nombreRepartidor;
    }

    public boolean isReservado() {
        return reservado;
    }

    public boolean isDespachado() {
        return despachado;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public boolean isEntregado() {
        return entregado;
    }

    public String getDescripcion() {
        return String.format("%s #%03d", getTipoEntrega(), idPedido);
    }

    protected void mostrarEncabezadoAsignacion() {
        System.out.println("[" + getDescripcion() + "]");
    }

    /**
     * Muestra la información común y los factores propios del tipo de pedido.
     */
    public void mostrarResumen() {
        System.out.println(getDescripcion());
        System.out.println("Dirección: " + direccionEntrega);
        System.out.println("Distancia: " + formatearDistancia() + " km");
        System.out.println("Factores: " + getFactoresDuracion());
        System.out.println("Repartidor asignado: " + getNombreRepartidor());
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
        mostrarEncabezadoAsignacion();
        System.out.println("Asignando repartidor para pedido de tipo: "
                + getTipoEntrega());
    }

    /**
     * Sobrecarga del método asignarRepartidor().
     * Esta versión recibe el nombre del repartidor.
     */
    public void asignarRepartidor(String nombreRepartidor) {
        mostrarEncabezadoAsignacion();
        System.out.println("Asignando repartidor para pedido de tipo: "
                + getTipoEntrega());

        if (confirmarAsignacion(nombreRepartidor)) {
            System.out.println("-> ASIGNADO a " + nombreRepartidor);
        }
    }

    /**
     * Sobrecarga que recibe los datos de un repartidor candidato.
     */
    public void asignarRepartidor(Repartidor repartidor) {
        mostrarEncabezadoAsignacion();

        if (repartidor == null) {
            System.out.println("-> NO ASIGNADO: el repartidor no es válido.");
            return;
        }

        if (!repartidor.isDisponible()) {
            System.out.println("-> NO ASIGNADO: " + repartidor.getNombre()
                    + " no tiene disponibilidad inmediata.");
            return;
        }

        if (confirmarAsignacion(repartidor.getNombre())) {
            System.out.println("-> ASIGNADO a " + repartidor.getNombre());
        }
    }

    /**
     * Sobrecarga que permite evaluar varios repartidores candidatos.
     */
    public void asignarRepartidor(Repartidor[] repartidores) {
        mostrarEncabezadoAsignacion();

        if (repartidores == null || repartidores.length == 0) {
            System.out.println("-> NO ASIGNADO: no hay repartidores candidatos.");
            return;
        }

        for (Repartidor repartidor : repartidores) {
            if (repartidor != null && repartidor.isDisponible()
                    && repartidor.tieneNombreValido()) {
                asignarRepartidor(repartidor);
                return;
            }
        }

        System.out.println("-> NO ASIGNADO: no hay candidatos disponibles "
                + "con nombre válido.");
    }

    public void reservar() {
        System.out.println("Reservando " + getDescripcion() + "...");

        if (cancelado) {
            System.out.println("-> RESERVA RECHAZADA: el pedido está cancelado.");
            return;
        }

        if (despachado) {
            System.out.println("-> RESERVA RECHAZADA: el pedido ya fue despachado.");
            return;
        }

        if (reservado) {
            System.out.println("-> El pedido ya se encuentra reservado.");
            return;
        }

        reservado = true;
        System.out.println("-> Pedido reservado correctamente.");
    }

    @Override
    public void despachar() {
        System.out.println("Despachando " + getDescripcion() + "...");

        if (cancelado) {
            System.out.println("-> DESPACHO RECHAZADO: el pedido está cancelado.");
            return;
        }

        if (!reservado) {
            System.out.println("-> DESPACHO RECHAZADO: el pedido no está reservado.");
            return;
        }

        if (nombreRepartidor == null) {
            System.out.println("-> DESPACHO RECHAZADO: no hay repartidor asignado.");
            return;
        }

        if (despachado) {
            System.out.println("-> El pedido ya fue despachado.");
            return;
        }

        despachado = true;
        System.out.println("-> Pedido despachado correctamente por "
                + nombreRepartidor + ".");
    }

    @Override
    public void cancelar() {
        System.out.println("Cancelando " + getDescripcion() + "...");

        if (despachado) {
            System.out.println("-> CANCELACIÓN RECHAZADA: el pedido ya fue despachado.");
            return;
        }

        if (cancelado) {
            System.out.println("-> El pedido ya se encuentra cancelado.");
            return;
        }

        cancelado = true;
        reservado = false;
        System.out.println("-> Pedido cancelado exitosamente.");
    }

    protected boolean confirmarAsignacion(String nombreRepartidor) {
        if (nombreRepartidor == null || nombreRepartidor.isBlank()) {
            System.out.println("-> NO ASIGNADO: el nombre del repartidor no es válido.");
            return false;
        }

        if (cancelado || despachado) {
            System.out.println("-> NO ASIGNADO: el pedido no admite nuevas asignaciones.");
            return false;
        }

        this.nombreRepartidor = nombreRepartidor;
        return true;
    }

    boolean marcarEntregado() {
        if (!despachado || cancelado || entregado) {
            return false;
        }

        entregado = true;
        return true;
    }
}
