package modelo;

public class Repartidor {
    private final int id;
    private final String nombre;

    public Repartidor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }

    public String getNombre() { return nombre; }

    @Override
    public String toString() { return id + " - " + nombre; }
}
