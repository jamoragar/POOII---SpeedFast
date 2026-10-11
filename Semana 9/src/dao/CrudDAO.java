package dao;
import modelo.Entidad;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
/** El servicio aporta la conexión para agrupar varias operaciones en una transacción. */
public interface CrudDAO<T extends Entidad> {
    int create(Connection cn,T entidad) throws SQLException;
    List<T> readAll(Connection cn) throws SQLException;
    T findById(Connection cn,int id,boolean bloquear) throws SQLException;
    boolean update(Connection cn,T entidad) throws SQLException;
    boolean delete(Connection cn,int id) throws SQLException;
}
