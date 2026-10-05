package controlador;
import dao.EntregaDAO;
import dao.impl.EntregaDAOImpl;
import modelo.Entrega;
import java.sql.SQLException;
import java.util.List;

/** Conecta la vista con el contrato DAO, sin incluir SQL ni componentes Swing. */
public class EntregaControlador {
    private final EntregaDAO dao = new EntregaDAOImpl();
    public int create(Entrega entidad) throws SQLException { return dao.create(entidad); }
    public List<Entrega> readAll() throws SQLException { return dao.readAll(); }
    public boolean update(Entrega entidad) throws SQLException { return dao.update(entidad); }
    public boolean delete(int id) throws SQLException { return dao.delete(id); }
}
