package dao.impl;
import dao.UsuarioDAO;
import modelo.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public final class UsuarioDAOImpl extends JdbcDAO<Usuario> implements UsuarioDAO {
    public UsuarioDAOImpl() { super("usuarios", "nombre", "rut", "correo", "`contraseña`", "rol"); }
    @Override protected Usuario mapear(ResultSet rs) throws SQLException { return "bibliotecario".equals(rs.getString("rol"))?
            new Bibliotecario(rs.getInt("id"),rs.getString("nombre"),rs.getString("rut"),rs.getString("correo"),rs.getString("contraseña")):
            new UsuarioEstudiante(rs.getInt("id"),rs.getString("nombre"),rs.getString("rut"),rs.getString("correo"),rs.getString("contraseña"),-1); }
    @Override protected void parametros(PreparedStatement ps,Usuario entidad) throws SQLException {
        ps.setString(1,entidad.getNombre());
        ps.setString(2,entidad.getRut());
        ps.setString(3,entidad.getCorreo());
        ps.setString(4,entidad.getClave());
        ps.setString(5,entidad.getRol());
    }
    @Override public Usuario findByRut(Connection cn,String rut) throws SQLException {
        var lista=consultar(cn," WHERE REPLACE(UPPER(rut), '.', '') = ?",ps->ps.setString(1,rut));
        return lista.isEmpty()?null:lista.get(0);
    }
    @Override public void bloquearBibliotecarios(Connection cn) throws SQLException {
        try(PreparedStatement ps=cn.prepareStatement("SELECT id FROM usuarios WHERE rol = ? ORDER BY id FOR UPDATE")) {
            ps.setString(1,"bibliotecario");try(ResultSet rs=ps.executeQuery()) { while(rs.next()) rs.getInt(1); }
        }
    }
    @Override public int contarBibliotecarios(Connection cn) throws SQLException {
        try(PreparedStatement ps=cn.prepareStatement("SELECT COUNT(*) FROM usuarios WHERE rol = ?")) {
            ps.setString(1,"bibliotecario");try(ResultSet rs=ps.executeQuery()) { rs.next();return rs.getInt(1); }
        }
    }
}
