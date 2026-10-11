package dao.impl;
import dao.LibroDAO;
import modelo.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public final class LibroDAOImpl extends JdbcDAO<Libro> implements LibroDAO {
    public LibroDAOImpl() { super("libros", "titulo", "autor", "isbn", "editorial", "stock", "id_categoria"); }
    @Override protected Libro mapear(ResultSet rs) throws SQLException { return new Libro(rs.getInt("id"),rs.getString("titulo"),rs.getString("autor"),rs.getString("isbn"),rs.getString("editorial"),rs.getInt("stock"),rs.getInt("id_categoria")); }
    @Override protected void parametros(PreparedStatement ps,Libro entidad) throws SQLException {
        ps.setString(1,entidad.getTitulo());
        ps.setString(2,entidad.getAutor());
        ps.setString(3,entidad.getIsbn());
        ps.setString(4,entidad.getEditorial());
        ps.setInt(5,entidad.getStock());
        ps.setInt(6,entidad.getIdCategoria());
    }
    @Override public void cambiarStock(Connection cn,int id,int diferencia) throws SQLException {
        try(PreparedStatement ps=cn.prepareStatement("UPDATE libros SET stock = stock + ? WHERE id = ? AND stock + ? >= 0")) {
            ps.setInt(1,diferencia);ps.setInt(2,id);ps.setInt(3,diferencia);
            if(ps.executeUpdate()!=1) throw new IllegalArgumentException("No hay ejemplares disponibles o el libro ya no existe.");
        }
    }
}
