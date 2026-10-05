package dao;
import modelo.Entrega;
import java.sql.SQLException;
import java.util.List;

/** Contrato de persistencia: los errores SQL se propagan para informar a la vista. */
public interface EntregaDAO {
    /** Devuelve el ID autogenerado por MySQL. */
    int create(Entrega entidad) throws SQLException;
    /** Una lista vacía significa consulta correcta sin filas; un fallo lanza SQLException. */
    List<Entrega> readAll() throws SQLException;
    /** Devuelve false si el ID ya no existe. */
    boolean update(Entrega entidad) throws SQLException;
    /** Devuelve false si el ID ya no existe; las referencias existentes provocan SQLException. */
    boolean delete(int id) throws SQLException;
}
