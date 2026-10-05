package dao;
import modelo.Repartidor;
import java.sql.SQLException;
import java.util.List;

/** Contrato de persistencia: los errores SQL se propagan para informar a la vista. */
public interface RepartidorDAO {
    /** Devuelve el ID autogenerado por MySQL. */
    int create(Repartidor entidad) throws SQLException;
    /** Una lista vacía significa consulta correcta sin filas; un fallo lanza SQLException. */
    List<Repartidor> readAll() throws SQLException;
    /** Devuelve false si el ID ya no existe. */
    boolean update(Repartidor entidad) throws SQLException;
    /** Devuelve false si el ID ya no existe; las referencias existentes provocan SQLException. */
    boolean delete(int id) throws SQLException;
}
