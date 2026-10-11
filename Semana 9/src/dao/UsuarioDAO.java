package dao;
import modelo.Usuario;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
public interface UsuarioDAO extends CrudDAO<Usuario> {
    Usuario findByRut(Connection cn,String rut) throws SQLException;
    void bloquearBibliotecarios(Connection cn) throws SQLException;
    int contarBibliotecarios(Connection cn) throws SQLException;
}
