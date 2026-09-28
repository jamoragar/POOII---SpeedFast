package dao;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/speedfast_db";
    private static final String USER = "root";

    public static Connection conectar() throws SQLException {
        Properties config = new Properties();
        Path archivo = Path.of(System.getProperty("speedfast.config", "config/db.properties"));
        if (Files.exists(archivo)) {
            try (Reader reader = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
                config.load(reader);
            } catch (IOException ex) {
                throw new SQLException("No se pudo leer config/db.properties.", ex);
            }
        }
        String url = valor("SPEEDFAST_DB_URL", config, "db.url", URL);
        String usuario = valor("SPEEDFAST_DB_USER", config, "db.user", USER);
        String clave = valor("SPEEDFAST_DB_PASSWORD", config, "db.password", null);
        if (clave == null) {
            throw new SQLException("Completa config/db.properties antes de conectar con MySQL.");
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Agrega MySQL Connector/J a las dependencias del módulo SpeedFast.", ex);
        }
        Properties opciones = new Properties();
        opciones.setProperty("user", usuario);
        opciones.setProperty("password", clave);
        opciones.setProperty("connectTimeout", "5000");
        opciones.setProperty("socketTimeout", "10000");
        return DriverManager.getConnection(url, opciones);
    }

    private static String valor(String variable, Properties config, String clave, String defecto) {
        String entorno = System.getenv(variable);
        return entorno != null ? entorno : config.getProperty(clave, defecto);
    }
}
