package modelo;
public final class UsuarioEstudiante extends Usuario {
    private final int idEstudiante;
    public UsuarioEstudiante(int id,String nombre,String rut,String correo,String clave,int idEstudiante) {
        super(id,nombre,rut,correo,clave); this.idEstudiante=idEstudiante;
    }
    public int getIdEstudiante() { return idEstudiante; }
    @Override public String getRol() { return "estudiante"; }
    @Override public boolean puedeRealizar(Accion accion) {
        return accion==Accion.CONSULTAR || accion==Accion.PRESTAR || accion==Accion.DEVOLVER;
    }
    @Override public boolean puedeRealizar(Accion accion,int propietario) {
        return puedeRealizar(accion) && idEstudiante>0 && propietario==idEstudiante;
    }
    @Override public Usuario conClave(String clave) { return new UsuarioEstudiante(getId(),getNombre(),getRut(),getCorreo(),clave,idEstudiante); }
}
