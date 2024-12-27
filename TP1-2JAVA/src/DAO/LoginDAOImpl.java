package DAO;

import Model.Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAOImpl implements LoginDAO {
    @Override
    public boolean validate(Login login) {
        boolean isValid = false;
        String query = "SELECT * FROM Login WHERE username = ? AND password = crypt(?, password)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login.getUsername());
            stmt.setString(2, login.getPassword());

            ResultSet rs = stmt.executeQuery();
            isValid = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
    }
}
