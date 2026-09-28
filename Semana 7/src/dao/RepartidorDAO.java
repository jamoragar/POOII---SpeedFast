package dao;

import modelo.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    public int guardar(Repartidor repartidor) throws SQLException {
        String sql = "INSERT INTO repartidor (nombre) VALUES (?)";
        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, repartidor.getNombre());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) { return rs.getInt(1); }
                throw new SQLException("MySQL no devolvió el ID del repartidor.");
            }
        }
    }

    public List<Repartidor> listarTodos() throws SQLException {
        List<Repartidor> repartidores = new ArrayList<>();
        String sql = "SELECT r.id, r.nombre, NOT EXISTS (SELECT 1 FROM entrega e "
                + "JOIN pedido p ON p.id = e.id_pedido WHERE e.id_repartidor = r.id "
                + "AND p.estado = 'EN_REPARTO') AS disponible FROM repartidor r ORDER BY r.id";
        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                repartidores.add(new Repartidor(rs.getInt("id"), rs.getString("nombre"),
                        rs.getBoolean("disponible")));
            }
        }
        return repartidores;
    }
}
