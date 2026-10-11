package modelo;
/** Abstracción que comparte identidad y datos personales, sin depender de Swing o JDBC. */
public abstract class Persona implements Entidad {
    private final int id;
    private final String nombre, rut, correo;
    protected Persona(int id, String nombre, String rut, String correo) {
        this.id=id; this.nombre=nombre; this.rut=rut; this.correo=correo;
    }
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getRut() { return rut; }
    public String getCorreo() { return correo; }
    public abstract String descripcion();
    @Override public String toString() { return id + " - " + descripcion(); }
}
