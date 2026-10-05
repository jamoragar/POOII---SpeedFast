package controlador;
import dao.PedidoDAO;
import dao.impl.PedidoDAOImpl;
import modelo.Pedido;
import java.sql.SQLException;
import java.util.List;

/** Conecta la vista con el contrato DAO, sin incluir SQL ni componentes Swing. */
public class PedidoControlador {
    private final PedidoDAO dao = new PedidoDAOImpl();
    public int create(Pedido entidad) throws SQLException { return dao.create(entidad); }
    public List<Pedido> readAll() throws SQLException { return dao.readAll(); }
    public boolean update(Pedido entidad) throws SQLException { return dao.update(entidad); }
    public boolean delete(int id) throws SQLException { return dao.delete(id); }
}
