package dao.impl;
import dao.ReporteDAO;
import modelo.Reporte;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public final class ReporteDAOImpl implements ReporteDAO {
    @Override public Reporte masPrestados(Connection cn) throws SQLException {
        String sql="SELECT l.id,l.titulo,COUNT(p.id) AS cantidad FROM libros l LEFT JOIN prestamos p ON p.id_libro=l.id GROUP BY l.id,l.titulo ORDER BY cantidad DESC,l.titulo";
        return consultar(cn,sql,new String[]{"ID","Libro","Préstamos"},null);
    }
    private static final String DETALLE="SELECT p.id,e.nombre,l.titulo,p.fecha_prestamo,p.fecha_devolucion,p.devuelto,p.fecha_devolucion_real, "+
        "CASE WHEN p.devuelto AND p.fecha_devolucion_real IS NULL THEN 'Sin fecha registrada' "+
        "ELSE CONCAT(GREATEST(0,DATEDIFF(COALESCE(p.fecha_devolucion_real,CURRENT_DATE),p.fecha_devolucion)), ' día(s)') END "+
        "FROM prestamos p JOIN estudiantes e ON e.id=p.id_estudiante JOIN libros l ON l.id=p.id_libro ";
    private static final String[] COLUMNAS={"ID","Estudiante","Libro","Préstamo","Vencimiento","Devuelto","Devolución real","Atraso"};
    @Override public Reporte historial(Connection cn,int estudiante) throws SQLException {
        return consultar(cn,DETALLE+"WHERE p.id_estudiante = ? ORDER BY p.fecha_prestamo DESC,p.id DESC",COLUMNAS,estudiante);
    }
    @Override public Reporte activos(Connection cn) throws SQLException {
        return consultar(cn,DETALLE+"WHERE p.devuelto = FALSE ORDER BY p.fecha_devolucion,p.id",COLUMNAS,null);
    }
    private Reporte consultar(Connection cn,String sql,String[] columnas,Integer id) throws SQLException {
        List<Object[]> filas=new ArrayList<>();
        try(PreparedStatement ps=cn.prepareStatement(sql)) {
            if(id!=null)ps.setInt(1,id);
            try(ResultSet rs=ps.executeQuery()) { while(rs.next()) {
                Object[] fila=new Object[columnas.length];for(int i=0;i<fila.length;i++)fila[i]=rs.getObject(i+1);filas.add(fila);
            }}
        }return new Reporte(columnas.clone(),filas);
    }
}
