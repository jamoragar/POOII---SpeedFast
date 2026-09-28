package modelo;

public class Pedido {

    private final int id;
    private final String direccionEntrega;
    private final TipoPedido tipo;
    private final EstadoPedido estado;
    private final Repartidor repartidor;

    public Pedido(String direccion, TipoPedido tipo) {
        this(0, direccion, tipo, EstadoPedido.PENDIENTE, null);
    }

    public Pedido(int id, String direccion, TipoPedido tipo, EstadoPedido estado, Repartidor repartidor) {
        if (direccion == null || direccion.isBlank() || direccion.trim().length() > 150) {
            throw new IllegalArgumentException("La dirección es obligatoria y admite hasta 150 caracteres.");
        }
        if (tipo == null || estado == null) {
            throw new IllegalArgumentException("Selecciona un tipo de pedido válido.");
        }
        this.id = id;
        this.direccionEntrega = direccion.trim();
        this.tipo = tipo;
        this.estado = estado;
        this.repartidor = repartidor;
    }

    public int getId() { return id; }
    public String getDireccionEntrega() { return direccionEntrega; }
    public TipoPedido getTipo() { return tipo; }
    public EstadoPedido getEstado() { return estado; }
    public Repartidor getRepartidor() { return repartidor; }
}
