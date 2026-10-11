package modelo;
public interface Autorizable {
    boolean puedeRealizar(Accion accion);
    boolean puedeRealizar(Accion accion, int idEstudiante);
}
