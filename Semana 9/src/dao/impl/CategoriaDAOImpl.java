package dao.impl;
import dao.CategoriaDAO;
import modelo.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public final class CategoriaDAOImpl extends JdbcDAO<Categoria> implements CategoriaDAO {
    public CategoriaDAOImpl() { super("categorias", "nombre"); }
    @Override protected Categoria mapear(ResultSet rs) throws SQLException { return new Categoria(rs.getInt("id"),rs.getString("nombre")); }
    @Override protected void parametros(PreparedStatement ps,Categoria entidad) throws SQLException {
        ps.setString(1,entidad.getNombre());
    }

}
