package modelo;
public final class Estudiante extends Persona {
    private final String curso;
    public Estudiante(int id,String nombre,String rut,String correo,String curso) { super(id,nombre,rut,correo);this.curso=curso; }
    public String getCurso() { return curso; }
    @Override public String descripcion() { return getNombre() + " — " + curso; }
}
