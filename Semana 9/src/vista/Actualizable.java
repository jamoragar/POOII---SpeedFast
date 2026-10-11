package vista;
/** Permite refrescar ventanas relacionadas sin conocer su entidad. */
public interface Actualizable {
    void refrescar();
    boolean estaOcupado();
}
