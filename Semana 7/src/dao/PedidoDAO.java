package dao;

import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;
import modelo.TipoPedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public int guardar(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (direccion, tipo, estado) VALUES (?, ?, ?)";
        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, pedido.getDireccionEntrega());
            ps.setString(2, pedido.getTipo().name());
            ps.setString(3, EstadoPedido.PENDIENTE.name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) { return rs.getInt(1); }
                throw new SQLException("MySQL no devolvió el ID del pedido.");
            }
        }
    }

    public List<Pedido> listarTodos() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, r.id AS repartidor_id, r.nombre AS repartidor_nombre "
                + "FROM pedido p LEFT JOIN entrega e ON e.id = "
                + "(SELECT MAX(ultima.id) FROM entrega ultima WHERE ultima.id_pedido = p.id) "
                + "LEFT JOIN repartidor r ON r.id = e.id_repartidor ORDER BY p.id";
        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                EstadoPedido estado = EstadoPedido.valueOf(rs.getString("estado"));
                int idRepartidor = rs.getInt("repartidor_id");
                Repartidor repartidor = rs.wasNull() ? null : new Repartidor(idRepartidor,
                        rs.getString("repartidor_nombre"), estado != EstadoPedido.EN_REPARTO);
                pedidos.add(new Pedido(rs.getInt("id"), rs.getString("direccion"),
                        TipoPedido.valueOf(rs.getString("tipo")), estado, repartidor));
            }
        }
        return pedidos;
    }

    public void completarEntrega(int idPedido) throws SQLException {
        String sql = "UPDATE pedido SET estado = 'ENTREGADO' WHERE id = ? AND estado = 'EN_REPARTO'";
        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            if (ps.executeUpdate() != 1) {
                throw new SQLException("El pedido ya no está en reparto. Refresca la tabla.");
            }
        }
    }
}
