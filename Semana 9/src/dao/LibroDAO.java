package dao;
import modelo.Libro;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
public interface LibroDAO extends CrudDAO<Libro> {
    void cambiarStock(Connection cn,int id,int diferencia) throws SQLException;
}
