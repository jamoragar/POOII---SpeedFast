package servicio;

import dao.EstudianteDAO;
import dao.UsuarioDAO;
import dao.impl.EstudianteDAOImpl;
import dao.impl.UsuarioDAOImpl;
import modelo.*;
import util.Claves;
import util.DatabaseConnection;
import util.Validaciones;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

/** Autentica contra MySQL y vuelve a comprobar los permisos en cada operación. */
public final class SeguridadServicio {
    private final UsuarioDAO usuarios=new UsuarioDAOImpl();
    private final EstudianteDAO estudiantes=new EstudianteDAOImpl();

    public Sesion ingresar(String rut,char[] clave) throws SQLException {
        String normalizado=Validaciones.rut(rut);
        try {
            return DatabaseConnection.getInstance().transaccion(cn -> {
                Usuario usuario=usuarios.findByRut(cn,normalizado);
                if(usuario!=null) usuario=usuarios.findById(cn,usuario.getId(),true);
                if(usuario==null || !usuario.verificarClave(clave))
                    throw new IllegalArgumentException("RUT o contraseña incorrectos.");
                usuario=vincular(cn,usuario);
                // Migración compatible con los datos académicos originales, sin alterar el SQL fuente.
                if(!usuario.getClave().startsWith("pbkdf2$")) {
                    usuario=usuario.conClave(Claves.hash(clave));
                    usuarios.update(cn,usuario);
                }
                return new Sesion(usuario);
            });
        } finally { Arrays.fill(clave,'\0'); }
    }

    public Usuario actual(Connection cn,Sesion sesion) throws SQLException {
        if(sesion==null) throw new SecurityException("Debes iniciar sesión.");
        Usuario usuario=usuarios.findById(cn,sesion.usuario().getId(),false);
        if(usuario==null) throw new SecurityException("La cuenta ya no existe. Cierra sesión.");
        return vincular(cn,usuario);
    }

    private Usuario vincular(Connection cn,Usuario usuario) throws SQLException {
        if(usuario instanceof UsuarioEstudiante) {
            Estudiante ficha=estudiantes.findByRut(cn,Validaciones.rut(usuario.getRut()));
            if(ficha==null) throw new SecurityException("La cuenta no tiene ficha de estudiante. Contacta al bibliotecario.");
            return new UsuarioEstudiante(usuario.getId(),usuario.getNombre(),usuario.getRut(),usuario.getCorreo(),usuario.getClave(),ficha.getId());
        }
        return usuario;
    }

    public Usuario exigir(Connection cn,Sesion sesion,Accion accion) throws SQLException {
        Usuario usuario=actual(cn,sesion);
        if(!usuario.puedeRealizar(accion)) throw new SecurityException("Tu rol no permite esta operación.");
        return usuario;
    }

    public void exigir(Connection cn,Sesion sesion,Accion accion,int propietario) throws SQLException {
        if(!actual(cn,sesion).puedeRealizar(accion,propietario))
            throw new SecurityException("Solo puedes consultar y gestionar tus propios préstamos.");
    }
}
