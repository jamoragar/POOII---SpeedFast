package modelo;
import util.Claves;
/** Dos contratos independientes: autenticación y autorización polimórfica. */
public abstract class Usuario extends Persona implements Autenticable, Autorizable {
    private final String clave;
    protected Usuario(int id, String nombre, String rut, String correo, String clave) {
        super(id,nombre,rut,correo); this.clave=clave;
    }
    public String getClave() { return clave; }
    public abstract String getRol();
    @Override public boolean verificarClave(char[] candidata) { return Claves.verificar(candidata,clave); }
    @Override public String descripcion() { return getNombre() + " (" + getRol() + ")"; }
    public abstract Usuario conClave(String nuevaClave);
}
