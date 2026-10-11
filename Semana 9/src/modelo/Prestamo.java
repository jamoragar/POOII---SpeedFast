package modelo;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
public final class Prestamo implements Entidad {
    private final int id;
    private final int idEstudiante;
    private final int idLibro;
    private final LocalDate fechaPrestamo;
    private final LocalDate fechaDevolucion;
    private final boolean devuelto;
    private final LocalDate fechaDevolucionReal;
    public Prestamo(int id, int idEstudiante, int idLibro, LocalDate fechaPrestamo, LocalDate fechaDevolucion, boolean devuelto, LocalDate fechaDevolucionReal) {
        this.id=id;
        this.idEstudiante=idEstudiante;
        this.idLibro=idLibro;
        this.fechaPrestamo=fechaPrestamo;
        this.fechaDevolucion=fechaDevolucion;
        this.devuelto=devuelto;
        this.fechaDevolucionReal=fechaDevolucionReal;
    }
    public int getId() { return id; }
    public int getIdEstudiante() { return idEstudiante; }
    public int getIdLibro() { return idLibro; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public boolean getDevuelto() { return devuelto; }
    public LocalDate getFechaDevolucionReal() { return fechaDevolucionReal; }
    /** Una fecha histórica desconocida no se interpreta como devolución puntual. */
    public String atraso(LocalDate hoy) {
        if (devuelto && fechaDevolucionReal==null) return "Sin fecha registrada";
        LocalDate fin=devuelto?fechaDevolucionReal:hoy;
        long dias=Math.max(0,ChronoUnit.DAYS.between(fechaDevolucion,fin));
        return dias==0?"A tiempo":dias+" día(s) de atraso";
    }
}
