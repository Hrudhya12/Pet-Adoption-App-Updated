package dao;

import database.DBConnection;
import model.Pet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDAO {

    public List<Pet> getAllPets() {
        List<Pet> pets = new ArrayList<>();

        String sql = "SELECT pet_id, name, type, breed, age, gender, size, personality, " +
                "health_status, is_adopted, adopter_username, adoption_date " +
                "FROM pets";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pet p = new Pet(
                        rs.getInt("pet_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("breed"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("size"),
                        rs.getString("personality"),
                        rs.getString("health_status"),
                        rs.getBoolean("is_adopted"),
                        rs.getString("adopter_username"),
                        rs.getTimestamp("adoption_date")
                );
                pets.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pets;
    }

    public Pet getPetById(int id) {

        String sql = "SELECT pet_id, name, type, breed, age, gender, size, personality, " +
                "health_status, is_adopted, adopter_username, adoption_date " +
                "FROM pets WHERE pet_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Pet(
                            rs.getInt("pet_id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getString("breed"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getString("size"),
                            rs.getString("personality"),
                            rs.getString("health_status"),
                            rs.getBoolean("is_adopted"),
                            rs.getString("adopter_username"),
                            rs.getTimestamp("adoption_date")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Pet> getPetsByType(String type) {
        List<Pet> pets = new ArrayList<>();

        String sql = "SELECT pet_id, name, type, breed, age, gender, size, personality, " +
                "health_status, is_adopted, adopter_username, adoption_date " +
                "FROM pets WHERE type = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pet p = new Pet(
                            rs.getInt("pet_id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getString("breed"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getString("size"),
                            rs.getString("personality"),
                            rs.getString("health_status"),
                            rs.getBoolean("is_adopted"),
                            rs.getString("adopter_username"),
                            rs.getTimestamp("adoption_date")
                    );
                    pets.add(p);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pets;
    }

    public List<Pet> searchPets(String type, String size, String personality) {
        List<Pet> pets = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT pet_id, name, type, breed, age, gender, size, personality, " +
                        "health_status, is_adopted, adopter_username, adoption_date " +
                        "FROM pets WHERE 1=1"
        );

        if (!type.isEmpty()) sql.append(" AND type LIKE ?");
        if (!size.isEmpty()) sql.append(" AND size LIKE ?");
        if (!personality.isEmpty()) sql.append(" AND personality LIKE ?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int idx = 1;

            if (!type.isEmpty()) stmt.setString(idx++, "%" + type + "%");
            if (!size.isEmpty()) stmt.setString(idx++, "%" + size + "%");
            if (!personality.isEmpty()) stmt.setString(idx++, "%" + personality + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pet p = new Pet(
                            rs.getInt("pet_id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getString("breed"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getString("size"),
                            rs.getString("personality"),
                            rs.getString("health_status"),
                            rs.getBoolean("is_adopted"),
                            rs.getString("adopter_username"),
                            rs.getTimestamp("adoption_date")
                    );
                    pets.add(p);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pets;
    }

    public void updatePet(Pet pet) {

        String sql = "UPDATE pets SET name = ?, type = ?, breed = ?, age = ?, gender = ?, " +
                "size = ?, personality = ?, health_status = ?, is_adopted = ?, " +
                "adopter_username = ?, adoption_date = ? WHERE pet_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pet.getName());
            stmt.setString(2, pet.getType());
            stmt.setString(3, pet.getBreed());
            stmt.setInt(4, pet.getAge());
            stmt.setString(5, pet.getGender());
            stmt.setString(6, pet.getSize());
            stmt.setString(7, pet.getPersonality());
            stmt.setString(8, pet.getHealthStatus());
            stmt.setBoolean(9, pet.isAdopted());
            stmt.setString(10, pet.getAdopterUsername());
            stmt.setTimestamp(11, pet.getAdoptionDate());
            stmt.setInt(12, pet.getPetId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pet> getAdoptedPets(String username) {
        List<Pet> pets = new ArrayList<>();

        String sql = "SELECT pet_id, name, type, breed, age, gender, size, personality, " +
                "health_status, is_adopted, adopter_username, adoption_date " +
                "FROM pets WHERE is_adopted = 1 AND adopter_username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pet p = new Pet(
                            rs.getInt("pet_id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getString("breed"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getString("size"),
                            rs.getString("personality"),
                            rs.getString("health_status"),
                            rs.getBoolean("is_adopted"),
                            rs.getString("adopter_username"),
                            rs.getTimestamp("adoption_date")
                    );
                    pets.add(p);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pets;
    }
}
