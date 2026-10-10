package com.travel.dao;

import com.travel.model.*;
import com.travel.util.DBConnection;
import com.travel.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** DAO for User entities. Maps role to the appropriate User subclass. */
public class UserDao implements Repository<User> {

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (name, email, password_hash, role, active) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getRole().name());
            stmt.setBoolean(5, true);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) user.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error saving user", e);
        }
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapUser(rs);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding user by id", e);
        }
        return null;
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapUser(rs);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding user by email", e);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding all users", e);
        }
        return users;
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, password_hash = ?, role = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getRole().name());
            stmt.setInt(5, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating user", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "UPDATE users SET active = false WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting user", e);
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String pass = rs.getString("password_hash");
        Role role = Role.valueOf(rs.getString("role"));
        
        switch (role) {
            case ADMIN: return new Admin(id, name, email, pass);
            case AGENT: return new TravelAgent(id, name, email, pass);
            case TRAVELER: return new Traveler(id, name, email, pass);
            default: throw new DataAccessException("Unknown role: " + role);
        }
    }
}
