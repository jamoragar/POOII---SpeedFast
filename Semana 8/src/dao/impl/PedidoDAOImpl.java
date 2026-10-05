package dao.impl;
import dao.PedidoDAO;
import modelo.*;
import util.ConexionDB;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** Consultas parametrizadas; cada operación cierra conexión, sentencia y resultado. */
public class PedidoDAOImpl implements PedidoDAO {
    @Override
    public int create(Pedido entidad) throws SQLException {
        String sql = "INSERT INTO pedidos (direccion, tipo, estado) VALUES (?, ?, ?)";
        try (Connection cn = ConexionDB.conectar();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entidad.getDireccion());
            ps.setString(2, entidad.getTipo().name());
            ps.setString(3, entidad.getEstado().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                throw new SQLException("No se obtuvo el ID generado.");
            }
        }
    }

    @Override
    public List<Pedido> readAll() throws SQLException {
        String sql = "SELECT id, direccion, tipo, estado FROM pedidos ORDER BY id";
        List<Pedido> lista = new ArrayList<>();
        try (Connection cn = ConexionDB.conectar(); PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Pedido(rs.getInt("id"), rs.getString("direccion"),
                    rs.getString("tipo") == null ? null : TipoPedido.valueOf(rs.getString("tipo")),
                    rs.getString("estado") == null ? null : EstadoPedido.valueOf(rs.getString("estado"))));
        }
        return lista;
    }

    @Override
    public boolean update(Pedido entidad) throws SQLException {
        String sql = "UPDATE pedidos SET direccion = ?, tipo = ?, estado = ? WHERE id = ?";
        try (Connection cn = ConexionDB.conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, entidad.getDireccion());
            ps.setString(2, entidad.getTipo().name());
            ps.setString(3, entidad.getEstado().name());
            ps.setInt(4, entidad.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM pedidos WHERE id = ?";
        try (Connection cn = ConexionDB.conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
