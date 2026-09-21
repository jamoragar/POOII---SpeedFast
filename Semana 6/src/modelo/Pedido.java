package modelo;

/** Modelo de Semana 5 adaptado al registro y la asignación desde ventanas. */
public class Pedido {
    private final int id;
    private final String direccionEntrega;
    private final TipoPedido tipo;
    private EstadoPedido estado = EstadoPedido.PENDIENTE;
    private Repartidor repartidor;

    public Pedido(int id, String direccionEntrega, TipoPedido tipo) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero.");
        }
        if (direccionEntrega == null || direccionEntrega.isBlank()) {
            throw new IllegalArgumentException("La dirección es obligatoria.");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("Selecciona un tipo de pedido.");
        }
        this.id = id;
        this.direccionEntrega = direccionEntrega.trim();
        this.tipo = tipo;
    }

    public int getId() { return id; }
    public String getDireccionEntrega() { return direccionEntrega; }
    public TipoPedido getTipo() { return tipo; }
    public EstadoPedido getEstado() { return estado; }
    public Repartidor getRepartidor() { return repartidor; }

    public void iniciarEntrega(Repartidor asignado) {
        if (estado != EstadoPedido.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden iniciar pedidos pendientes.");
        }
        if (asignado == null) {
            throw new IllegalArgumentException("Selecciona un repartidor disponible.");
        }
        asignado.reservar();
        repartidor = asignado;
        estado = EstadoPedido.EN_REPARTO;
    }

    public void completarEntrega() {
        if (estado != EstadoPedido.EN_REPARTO) {
            throw new IllegalStateException("El pedido no tiene una entrega activa.");
        }
        estado = EstadoPedido.ENTREGADO;
        repartidor.liberar();
    }

    public void revertirEntrega() {
        if (estado != EstadoPedido.EN_REPARTO) {
            throw new IllegalStateException("El pedido no tiene una entrega activa.");
        }
        repartidor.liberar();
        repartidor = null;
        estado = EstadoPedido.PENDIENTE;
    }
}
