package dao.impl;
import dao.PrestamoDAO;
import modelo.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public final class PrestamoDAOImpl extends JdbcDAO<Prestamo> implements PrestamoDAO {
    public PrestamoDAOImpl() { super("prestamos", "id_estudiante", "id_libro", "fecha_prestamo", "fecha_devolucion", "devuelto", "fecha_devolucion_real"); }
    @Override protected Prestamo mapear(ResultSet rs) throws SQLException { return new Prestamo(rs.getInt("id"),rs.getInt("id_estudiante"),rs.getInt("id_libro"),rs.getObject("fecha_prestamo",LocalDate.class),rs.getObject("fecha_devolucion",LocalDate.class),rs.getBoolean("devuelto"),rs.getObject("fecha_devolucion_real",LocalDate.class)); }
    @Override protected void parametros(PreparedStatement ps,Prestamo entidad) throws SQLException {
        ps.setInt(1,entidad.getIdEstudiante());
        ps.setInt(2,entidad.getIdLibro());
        ps.setObject(3,entidad.getFechaPrestamo());
        ps.setObject(4,entidad.getFechaDevolucion());
        ps.setBoolean(5,entidad.getDevuelto());
        ps.setObject(6,entidad.getFechaDevolucionReal());
    }
    @Override public boolean existeActivo(Connection cn,int estudiante,int libro,int excluir) throws SQLException {
        try(PreparedStatement ps=cn.prepareStatement("SELECT id FROM prestamos WHERE id_estudiante = ? AND id_libro = ? AND devuelto = FALSE AND id <> ? FOR UPDATE")) {
            ps.setInt(1,estudiante);ps.setInt(2,libro);ps.setInt(3,excluir);
            try(ResultSet rs=ps.executeQuery()) { return rs.next(); }
        }
    }
    @Override public List<Prestamo> porEstudiante(Connection cn,int estudiante) throws SQLException {
        return consultar(cn," WHERE id_estudiante = ? ORDER BY id",ps->ps.setInt(1,estudiante));
    }
}
