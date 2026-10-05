package controlador;
import dao.RepartidorDAO;
import dao.impl.RepartidorDAOImpl;
import modelo.Repartidor;
import java.sql.SQLException;
import java.util.List;

/** Conecta la vista con el contrato DAO, sin incluir SQL ni componentes Swing. */
public class RepartidorControlador {
    private final RepartidorDAO dao = new RepartidorDAOImpl();
    public int create(Repartidor entidad) throws SQLException { return dao.create(entidad); }
    public List<Repartidor> readAll() throws SQLException { return dao.readAll(); }
    public boolean update(Repartidor entidad) throws SQLException { return dao.update(entidad); }
    public boolean delete(int id) throws SQLException { return dao.delete(id); }
}
