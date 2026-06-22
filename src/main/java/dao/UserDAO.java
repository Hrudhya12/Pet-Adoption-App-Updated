package dao;

import database.DBConnection;
import model.User;

import java.sql.*;

public class UserDAO {

    // REGISTER USER
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (name, email, password, age, preferred_pet_type, preferred_size, preferred_personality) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setInt(4, user.getAge());
            stmt.setString(5, user.getPreferredPetType());
            stmt.setString(6, user.getPreferredSize());
            stmt.setString(7, user.getPreferredPersonality());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    // LOGIN USER
    public static User loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setAge(rs.getInt("age"));
                user.setPreferredPetType(rs.getString("preferred_pet_type"));
                user.setPreferredSize(rs.getString("preferred_size"));
                user.setPreferredPersonality(rs.getString("preferred_personality"));

                // username = email
                user.setUsername(rs.getString("email"));

                return user;
            }

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }

        return null;
    }

    // CHECK IF EMAIL EXISTS
    public boolean emailExists(String email) {
        String sql = "SELECT email FROM users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Error checking email: " + e.getMessage());
            return true;
        }
    }
}
