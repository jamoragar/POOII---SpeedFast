package servicio;

import dao.*;
import dao.impl.*;
import modelo.*;
import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

/** Unidad de negocio compartida: préstamo e inventario se confirman juntos. */
public final class PrestamoServicio {
    private static final PrestamoServicio INSTANCE=new PrestamoServicio();
    public static PrestamoServicio getInstance() { return INSTANCE; }
    private PrestamoServicio() { }
    private final DatabaseConnection db=DatabaseConnection.getInstance();
    private final SeguridadServicio seguridad=new SeguridadServicio();
    private final PrestamoDAO prestamos=new PrestamoDAOImpl();
    private final LibroDAO libros=new LibroDAOImpl();
    private final EstudianteDAO estudiantes=new EstudianteDAOImpl();

    public synchronized int registrar(Sesion s,int estudiante,int libro) throws SQLException {
        return registrar(s,new Prestamo(0,estudiante,libro,LocalDate.now(),LocalDate.now().plusDays(7),false,null));
    }
    public synchronized int registrar(Sesion s,Prestamo dato) throws SQLException {
        validar(dato);
        return db.transaccion(c -> {
            Usuario u=seguridad.actual(c,s);
            seguridad.exigir(c,s,Accion.PRESTAR,dato.getIdEstudiante());
            Prestamo nuevo=u instanceof UsuarioEstudiante?
                new Prestamo(0,dato.getIdEstudiante(),dato.getIdLibro(),LocalDate.now(),LocalDate.now().plusDays(7),false,null):dato;
            bloquearLibro(c,nuevo.getIdLibro());
            BibliotecaServicio.existe(estudiantes.findById(c,nuevo.getIdEstudiante(),false)!=null);
            if(prestamos.existeActivo(c,nuevo.getIdEstudiante(),nuevo.getIdLibro(),0)) throw new IllegalArgumentException("El estudiante ya tiene un préstamo activo de este libro.");
            libros.cambiarStock(c,nuevo.getIdLibro(),-1);
            // Esta fila constituye también el historial; si falla, rollback recupera el stock.
            return prestamos.create(c,nuevo);
        });
    }
    public synchronized void devolver(Sesion s,int id) throws SQLException {
        db.transaccion(c -> {
            Prestamo actual=prestamos.findById(c,id,true);BibliotecaServicio.existe(actual!=null);
            seguridad.exigir(c,s,Accion.DEVOLVER,actual.getIdEstudiante());
            if(actual.getDevuelto())throw new IllegalArgumentException("El préstamo ya fue devuelto.");
            if(LocalDate.now().isBefore(actual.getFechaPrestamo()))throw new IllegalArgumentException("La devolución no puede ser anterior al préstamo.");
            bloquearLibro(c,actual.getIdLibro());
            prestamos.update(c,new Prestamo(id,actual.getIdEstudiante(),actual.getIdLibro(),actual.getFechaPrestamo(),actual.getFechaDevolucion(),true,LocalDate.now()));
            libros.cambiarStock(c,actual.getIdLibro(),1);return null;
        });
    }
    public synchronized void actualizar(Sesion s,Prestamo dato) throws SQLException {
        validar(dato);
        db.transaccion(c -> {
            seguridad.exigir(c,s,Accion.ADMINISTRAR);
            Prestamo actual=prestamos.findById(c,dato.getId(),true);BibliotecaServicio.existe(actual!=null);
            if(actual.getDevuelto())throw new IllegalArgumentException("Solo se pueden editar préstamos activos.");
            // Orden fijo de bloqueo para limitar los interbloqueos al intercambiar libros.
            bloquearLibro(c,Math.min(actual.getIdLibro(),dato.getIdLibro()));
            if(actual.getIdLibro()!=dato.getIdLibro())bloquearLibro(c,Math.max(actual.getIdLibro(),dato.getIdLibro()));
            BibliotecaServicio.existe(estudiantes.findById(c,dato.getIdEstudiante(),false)!=null);
            if(prestamos.existeActivo(c,dato.getIdEstudiante(),dato.getIdLibro(),dato.getId()))throw new IllegalArgumentException("Ya existe otro préstamo activo de ese libro para el estudiante.");
            if(actual.getIdLibro()!=dato.getIdLibro()) {
                libros.cambiarStock(c,dato.getIdLibro(),-1);libros.cambiarStock(c,actual.getIdLibro(),1);
            }
            BibliotecaServicio.existe(prestamos.update(c,dato));return null;
        });
    }
    public synchronized void eliminar(Sesion s,int id) throws SQLException {
        db.transaccion(c -> {
            seguridad.exigir(c,s,Accion.ADMINISTRAR);
            Prestamo actual=prestamos.findById(c,id,true);BibliotecaServicio.existe(actual!=null);
            if(!actual.getDevuelto()) {bloquearLibro(c,actual.getIdLibro());libros.cambiarStock(c,actual.getIdLibro(),1);}
            BibliotecaServicio.existe(prestamos.delete(c,id));return null;
        });
    }
    private void bloquearLibro(Connection c,int id) throws SQLException {
        BibliotecaServicio.existe(libros.findById(c,id,true)!=null);
    }
    private void validar(Prestamo p) {
        if(p.getIdLibro()<=0 || p.getIdEstudiante()<=0)throw new IllegalArgumentException("Selecciona libro y estudiante.");
        if(p.getFechaPrestamo()==null || p.getFechaDevolucion()==null || p.getFechaDevolucion().isBefore(p.getFechaPrestamo()))
            throw new IllegalArgumentException("El vencimiento debe ser igual o posterior al préstamo.");
        if(p.getFechaPrestamo().isAfter(LocalDate.now())) throw new IllegalArgumentException("La fecha del préstamo no puede estar en el futuro.");
        if(p.getDevuelto() || p.getFechaDevolucionReal()!=null)throw new IllegalArgumentException("Usa la acción Devolver para registrar una devolución.");
    }
}
