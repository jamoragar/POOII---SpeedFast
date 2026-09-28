package modelo;

public enum TipoPedido {
    COMIDA("Comida"), ENCOMIENDA("Encomienda"), EXPRESS("Express");

    private final String etiqueta;

    TipoPedido(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
