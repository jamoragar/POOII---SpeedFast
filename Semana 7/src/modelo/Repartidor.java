package modelo;

public class Repartidor {

    private final int id;
    private final String nombre;
    private final boolean disponible;

    public Repartidor(String nombre) {
        this(0, nombre, true);
    }

    public Repartidor(int id, String nombre, boolean disponible) {
        if (nombre == null || nombre.isBlank() || nombre.trim().length() > 100) {
            throw new IllegalArgumentException("El nombre es obligatorio y admite hasta 100 caracteres.");
        }
        this.id = id;
        this.nombre = nombre.trim();
        this.disponible = disponible;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public boolean isDisponible() { return disponible; }

    @Override
    public String toString() {
        return nombre + (id > 0 ? " (#" + id + ")" : "");
    }
}
