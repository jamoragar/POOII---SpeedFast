package speedfast;

public class Pedido {

    private int id;
    private String direccionEntrega;
    private EstadoPedido estado;

    public Pedido(int id, String direccionEntrega) {
        this.id = id;
        this.direccionEntrega = direccionEntrega;
        this.estado = EstadoPedido.PENDIENTE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public synchronized EstadoPedido getEstado() {
        return estado;
    }

    public synchronized void setEstado(EstadoPedido nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }

        this.estado = nuevoEstado;
    }

    public synchronized void setEstado(String nuevoEstado) {
        setEstado(EstadoPedido.valueOf(nuevoEstado));
    }

    @Override
    public synchronized String toString() {
        return "Pedido #" + id + " - Destino: " + direccionEntrega
                + " - Estado: " + estado;
    }
}
