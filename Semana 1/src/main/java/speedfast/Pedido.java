package speedfast;

/**
 * Clase base del sistema de pedidos.
 * Representa un pedido genérico de SpeedFast.
 */
public class Pedido {

    private int idPedido;
    private String direccionEntrega;
    private String tipoPedido;

    public Pedido(int idPedido, String direccionEntrega, String tipoPedido) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.tipoPedido = tipoPedido;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getTipoPedido() {
        return tipoPedido;
    }

    public void setTipoPedido(String tipoPedido) {
        this.tipoPedido = tipoPedido;
    }

    /**
     * Método base que será sobrescrito por las clases hijas.
     */
    public void asignarRepartidor() {
        System.out.println("[PEDIDO GENÉRICO]");
        System.out.println("Asignando repartidor para pedido de tipo: "
                + tipoPedido);
    }

    /**
     * Sobrecarga del método asignarRepartidor().
     * Esta versión recibe el nombre del repartidor.
     */
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("[PEDIDO GENÉRICO]");
        System.out.println("Asignando repartidor para pedido de tipo: "
                + tipoPedido);
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
