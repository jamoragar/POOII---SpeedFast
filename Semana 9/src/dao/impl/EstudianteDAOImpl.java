package dao.impl;
import dao.EstudianteDAO;
import modelo.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public final class EstudianteDAOImpl extends JdbcDAO<Estudiante> implements EstudianteDAO {
    public EstudianteDAOImpl() { super("estudiantes", "nombre", "rut", "curso", "correo"); }
    @Override protected Estudiante mapear(ResultSet rs) throws SQLException { return new Estudiante(rs.getInt("id"),rs.getString("nombre"),rs.getString("rut"),rs.getString("correo"),rs.getString("curso")); }
    @Override protected void parametros(PreparedStatement ps,Estudiante entidad) throws SQLException {
        ps.setString(1,entidad.getNombre());
        ps.setString(2,entidad.getRut());
        ps.setString(3,entidad.getCurso());
        ps.setString(4,entidad.getCorreo());
    }
    @Override public Estudiante findByRut(Connection cn,String rut) throws SQLException {
        var lista=consultar(cn," WHERE REPLACE(UPPER(rut), '.', '') = ?",ps->ps.setString(1,rut));
        return lista.isEmpty()?null:lista.get(0);
    }
}
