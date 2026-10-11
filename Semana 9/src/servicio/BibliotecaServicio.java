package servicio;

import dao.*;
import dao.impl.*;
import modelo.*;
import util.Claves;
import util.DatabaseConnection;
import util.Validaciones;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/** Reglas de administración; el SQL permanece en los DAO. */
public final class BibliotecaServicio {
    private static final BibliotecaServicio INSTANCE=new BibliotecaServicio();
    public static BibliotecaServicio getInstance() { return INSTANCE; }
    private BibliotecaServicio() { }
    private final DatabaseConnection db=DatabaseConnection.getInstance();
    private final SeguridadServicio seguridad=new SeguridadServicio();
    private final CategoriaDAO categorias=new CategoriaDAOImpl();
    private final LibroDAO libros=new LibroDAOImpl();
    private final EstudianteDAO estudiantes=new EstudianteDAOImpl();
    private final UsuarioDAO usuarios=new UsuarioDAOImpl();
    private final PrestamoDAO prestamos=new PrestamoDAOImpl();
    private final ReporteDAO reportes=new ReporteDAOImpl();

    public List<Categoria> categorias(Sesion s) throws SQLException {
        return db.consultar(c -> {seguridad.exigir(c,s,Accion.CONSULTAR);return categorias.readAll(c);});
    }
    public List<Libro> libros(Sesion s) throws SQLException {
        return db.consultar(c -> {seguridad.exigir(c,s,Accion.CONSULTAR);return libros.readAll(c);});
    }
    public List<Estudiante> estudiantes(Sesion s) throws SQLException {
        return db.consultar(c -> {
            Usuario u=seguridad.exigir(c,s,Accion.CONSULTAR);
            if(u instanceof UsuarioEstudiante e) return List.of(estudiantes.findById(c,e.getIdEstudiante(),false));
            return estudiantes.readAll(c);
        });
    }
    public List<Usuario> usuarios(Sesion s) throws SQLException {
        return db.consultar(c -> {seguridad.exigir(c,s,Accion.ADMINISTRAR);return usuarios.readAll(c);});
    }
    public List<Prestamo> prestamos(Sesion s) throws SQLException {
        return db.consultar(c -> {
            Usuario u=seguridad.exigir(c,s,Accion.CONSULTAR);
            return u instanceof UsuarioEstudiante e?prestamos.porEstudiante(c,e.getIdEstudiante()):prestamos.readAll(c);
        });
    }
    public int crearCategoria(Sesion s,Categoria e) throws SQLException {
        Categoria dato=new Categoria(0,Validaciones.texto(e.getNombre(),"Nombre",100));
        return db.transaccion(c -> {seguridad.exigir(c,s,Accion.ADMINISTRAR);return categorias.create(c,dato);});
    }
    public void actualizarCategoria(Sesion s,Categoria e) throws SQLException {
        Categoria dato=new Categoria(e.getId(),Validaciones.texto(e.getNombre(),"Nombre",100));
        db.transaccion(c -> {seguridad.exigir(c,s,Accion.ADMINISTRAR);existe(categorias.update(c,dato));return null;});
    }
    public void eliminarCategoria(Sesion s,int id) throws SQLException {
        db.transaccion(c -> {seguridad.exigir(c,s,Accion.ADMINISTRAR);existe(categorias.delete(c,id));return null;});
    }
    private Libro validar(Libro e) {
        if(e.getStock()<0) throw new IllegalArgumentException("El stock no puede ser negativo.");
        if(e.getIdCategoria()<=0) throw new IllegalArgumentException("Selecciona una categoría.");
        return new Libro(e.getId(),Validaciones.texto(e.getTitulo(),"Título",200),Validaciones.texto(e.getAutor(),"Autor",100),
                Validaciones.texto(e.getIsbn(),"ISBN",20),Validaciones.texto(e.getEditorial(),"Editorial",100),e.getStock(),e.getIdCategoria());
    }
    public synchronized int crearLibro(Sesion s,Libro e) throws SQLException {
        Libro dato=validar(e);
        return db.transaccion(c -> {seguridad.exigir(c,s,Accion.ADMINISTRAR);return libros.create(c,dato);});
    }
    public synchronized void actualizarLibro(Sesion s,Libro anterior,Libro e) throws SQLException {
        Libro dato=validar(e);
        db.transaccion(c -> {
            seguridad.exigir(c,s,Accion.ADMINISTRAR);
            Libro actual=libros.findById(c,e.getId(),true);existe(actual!=null);
            if(actual.getStock()!=anterior.getStock()) throw new IllegalArgumentException("El stock cambió en otra operación. Limpia, refresca y selecciona nuevamente el libro.");
            existe(libros.update(c,dato));return null;
        });
    }
    public synchronized void eliminarLibro(Sesion s,int id) throws SQLException {
        db.transaccion(c -> {seguridad.exigir(c,s,Accion.ADMINISTRAR);existe(libros.delete(c,id));return null;});
    }
    private Estudiante validar(Estudiante e) {
        return new Estudiante(e.getId(),Validaciones.texto(e.getNombre(),"Nombre",100),Validaciones.rut(e.getRut()),
                Validaciones.correo(e.getCorreo()),Validaciones.texto(e.getCurso(),"Curso",20));
    }
    public int crearEstudiante(Sesion s,Estudiante e,char[] clave) throws SQLException {
        Estudiante dato=validar(e);Validaciones.clave(clave);String hash=Claves.hash(clave);
        return db.transaccion(c -> {
            seguridad.exigir(c,s,Accion.ADMINISTRAR);
            if(usuarios.findByRut(c,dato.getRut())!=null) throw new IllegalArgumentException("Ya existe una cuenta con ese RUT.");
            int id=estudiantes.create(c,dato);
            usuarios.create(c,new UsuarioEstudiante(0,dato.getNombre(),dato.getRut(),dato.getCorreo(),hash,id));
            return id;
        });
    }
    public void actualizarEstudiante(Sesion s,Estudiante e,char[] clave) throws SQLException {
        Estudiante dato=validar(e);String hash=clave.length==0?null:hashValidado(clave);
        db.transaccion(c -> {
            seguridad.exigir(c,s,Accion.ADMINISTRAR);
            Estudiante anterior=estudiantes.findById(c,e.getId(),true);existe(anterior!=null);
            Usuario usuario=usuarios.findByRut(c,Validaciones.rut(anterior.getRut()));
            Usuario otro=usuarios.findByRut(c,dato.getRut());
            if(otro!=null && (usuario==null || otro.getId()!=usuario.getId())) throw new IllegalArgumentException("El RUT pertenece a otra cuenta.");
            if(usuario!=null && !(usuario instanceof UsuarioEstudiante)) throw new IllegalArgumentException("El RUT está asociado a un bibliotecario.");
            existe(estudiantes.update(c,dato));
            if(usuario!=null) usuarios.update(c,new UsuarioEstudiante(usuario.getId(),dato.getNombre(),dato.getRut(),dato.getCorreo(),hash==null?usuario.getClave():hash,dato.getId()));
            else if(hash!=null) usuarios.create(c,new UsuarioEstudiante(0,dato.getNombre(),dato.getRut(),dato.getCorreo(),hash,dato.getId()));
            return null;
        });
    }
    public void eliminarEstudiante(Sesion s,int id) throws SQLException {
        db.transaccion(c -> {
            seguridad.exigir(c,s,Accion.ADMINISTRAR);Estudiante e=estudiantes.findById(c,id,true);existe(e!=null);
            Usuario u=usuarios.findByRut(c,Validaciones.rut(e.getRut()));
            if(u!=null && u.getId()==s.usuario().getId()) throw new IllegalArgumentException("No puedes eliminar tu cuenta en uso.");
            existe(estudiantes.delete(c,id));
            if(u instanceof UsuarioEstudiante) usuarios.delete(c,u.getId());
            return null;
        });
    }
    private String hashValidado(char[] clave) {Validaciones.clave(clave);return Claves.hash(clave);}
    private Usuario validar(Usuario u,String hash) {
        String nombre=Validaciones.texto(u.getNombre(),"Nombre",100),rut=Validaciones.rut(u.getRut()),correo=Validaciones.correo(u.getCorreo());
        return u instanceof Bibliotecario?new Bibliotecario(u.getId(),nombre,rut,correo,hash):new UsuarioEstudiante(u.getId(),nombre,rut,correo,hash,-1);
    }
    public int crearUsuario(Sesion s,Usuario u,char[] clave) throws SQLException {
        Usuario dato=validar(u,hashValidado(clave));
        return db.transaccion(c -> {
            seguridad.exigir(c,s,Accion.ADMINISTRAR);
            if(usuarios.findByRut(c,dato.getRut())!=null) throw new IllegalArgumentException("Ya existe una cuenta con ese RUT.");
            sincronizarFicha(c,null,dato);return usuarios.create(c,dato);
        });
    }
    public void actualizarUsuario(Sesion s,Usuario u,char[] clave) throws SQLException {
        Usuario datos=validar(u,clave.length==0?null:hashValidado(clave));
        db.transaccion(c -> {
            seguridad.exigir(c,s,Accion.ADMINISTRAR);usuarios.bloquearBibliotecarios(c);
            Usuario anterior=usuarios.findById(c,u.getId(),true);existe(anterior!=null);
            if(anterior.getId()==s.usuario().getId() && !anterior.getRol().equals(datos.getRol())) throw new IllegalArgumentException("No puedes cambiar el rol de tu cuenta en uso.");
            if(anterior instanceof Bibliotecario && !(datos instanceof Bibliotecario) && usuarios.contarBibliotecarios(c)<=1)
                throw new IllegalArgumentException("Debe permanecer al menos un bibliotecario.");
            Usuario otro=usuarios.findByRut(c,datos.getRut());
            if(otro!=null && otro.getId()!=datos.getId()) throw new IllegalArgumentException("El RUT pertenece a otra cuenta.");
            Usuario dato=datos.conClave(datos.getClave()==null?anterior.getClave():datos.getClave());
            sincronizarFicha(c,anterior,dato);existe(usuarios.update(c,dato));return null;
        });
    }
    /** Mantiene la unión por RUT sin duplicar la ficha del estudiante. */
    private void sincronizarFicha(Connection c,Usuario anterior,Usuario nuevo) throws SQLException {
        if(!(nuevo instanceof UsuarioEstudiante)) return;
        Estudiante ficha=anterior instanceof UsuarioEstudiante?estudiantes.findByRut(c,Validaciones.rut(anterior.getRut())):estudiantes.findByRut(c,nuevo.getRut());
        if(ficha==null) throw new IllegalArgumentException("Registra primero la ficha del estudiante con ese RUT.");
        Estudiante otra=estudiantes.findByRut(c,nuevo.getRut());
        if(otra!=null && otra.getId()!=ficha.getId()) throw new IllegalArgumentException("El RUT corresponde a otra ficha de estudiante.");
        estudiantes.update(c,new Estudiante(ficha.getId(),nuevo.getNombre(),nuevo.getRut(),nuevo.getCorreo(),ficha.getCurso()));
    }
    public void eliminarUsuario(Sesion s,int id) throws SQLException {
        db.transaccion(c -> {
            seguridad.exigir(c,s,Accion.ADMINISTRAR);usuarios.bloquearBibliotecarios(c);
            Usuario u=usuarios.findById(c,id,true);existe(u!=null);
            if(id==s.usuario().getId()) throw new IllegalArgumentException("No puedes eliminar tu cuenta en uso.");
            if(u instanceof Bibliotecario && usuarios.contarBibliotecarios(c)<=1) throw new IllegalArgumentException("Debe permanecer al menos un bibliotecario.");
            existe(usuarios.delete(c,id));return null;
        });
    }
    public Reporte reporte(Sesion s,String tipo,int estudiante) throws SQLException {
        return db.consultar(c -> {
            if("Historial por estudiante".equals(tipo)) {
                seguridad.exigir(c,s,Accion.CONSULTAR,estudiante);return reportes.historial(c,estudiante);
            }
            seguridad.exigir(c,s,Accion.REPORTES);
            return "Libros más prestados".equals(tipo)?reportes.masPrestados(c):reportes.activos(c);
        });
    }
    static void existe(boolean encontrado) {if(!encontrado)throw new IllegalArgumentException("El registro ya no existe. Refresca el listado.");}
}
