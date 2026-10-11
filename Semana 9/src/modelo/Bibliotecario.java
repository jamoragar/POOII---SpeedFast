package modelo;
public final class Bibliotecario extends Usuario {
    public Bibliotecario(int id,String nombre,String rut,String correo,String clave) { super(id,nombre,rut,correo,clave); }
    @Override public String getRol() { return "bibliotecario"; }
    @Override public boolean puedeRealizar(Accion accion) { return true; }
    // Sobrecarga usada cuando la operación corresponde al préstamo de un estudiante.
    @Override public boolean puedeRealizar(Accion accion,int idEstudiante) { return true; }
    @Override public Usuario conClave(String clave) { return new Bibliotecario(getId(),getNombre(),getRut(),getCorreo(),clave); }
}
