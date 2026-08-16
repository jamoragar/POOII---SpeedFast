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
        System.out.println("Pedido asignado a " + nombreRepartidor);
    }
}
