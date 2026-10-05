package dao;
import modelo.Pedido;
import java.sql.SQLException;
import java.util.List;

/** Contrato de persistencia: los errores SQL se propagan para informar a la vista. */
public interface PedidoDAO {
    /** Devuelve el ID autogenerado por MySQL. */
    int create(Pedido entidad) throws SQLException;
    /** Una lista vacía significa consulta correcta sin filas; un fallo lanza SQLException. */
    List<Pedido> readAll() throws SQLException;
    /** Devuelve false si el ID ya no existe. */
    boolean update(Pedido entidad) throws SQLException;
    /** Devuelve false si el ID ya no existe; las referencias existentes provocan SQLException. */
    boolean delete(int id) throws SQLException;
}
