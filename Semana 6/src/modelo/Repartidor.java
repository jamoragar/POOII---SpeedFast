package modelo;

/** Representa al repartidor; la espera de la entrega se ejecuta con SwingWorker. */
public class Repartidor {
    private final String nombre;
    private boolean disponible = true;

    public Repartidor(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del repartidor es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public String getNombre() { return nombre; }
    public boolean isDisponible() { return disponible; }

    public void reservar() {
        if (!disponible) {
            throw new IllegalArgumentException("El repartidor ya tiene una entrega activa.");
        }
        disponible = false;
    }

    public void liberar() { disponible = true; }

    @Override
    public String toString() { return nombre; }
}
