package DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/TPJAVA12";
    private static final String UTILISATEUR = "postgres";
    private static final String MOT_DE_PASSE = "aya@2004";
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Charger le driver JDBC PostgreSQL
                Class.forName("org.postgresql.Driver");
                
                // Établir la connexion
                connection = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de la connexion à la base de données.");
            }
        } else {
            try {
                // Vérifier si la connexion est fermée et la réouvrir si nécessaire
                if (connection != null && connection.isClosed()) {
                    connection = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
