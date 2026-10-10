package com.travel.dao;

import com.travel.model.Booking;
import com.travel.model.BookingStatus;
import com.travel.util.DBConnection;
import com.travel.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** DAO for Booking entities. Provides transactional save option. */
public class BookingDao implements Repository<Booking> {

    @Override
    public void save(Booking booking) {
        try (Connection conn = DBConnection.getConnection()) {
            save(booking, conn);
        } catch (SQLException e) {
            throw new DataAccessException("Error saving booking", e);
        }
    }

    public void save(Booking booking, Connection conn) throws SQLException {
        String sql = "INSERT INTO bookings (traveler_id, listing_id, status, start_date, end_date, booked_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getTravelerId());
            stmt.setInt(2, booking.getListingId());
            stmt.setString(3, booking.getStatus().name());
            stmt.setDate(4, Date.valueOf(booking.getStartDate()));
            stmt.setDate(5, Date.valueOf(booking.getEndDate()));
            stmt.setTimestamp(6, Timestamp.valueOf(booking.getBookedAt()));
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) booking.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public Booking findById(int id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapBooking(rs);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding booking by id", e);
        }
        return null;
    }

    @Override
    public List<Booking> findAll() {
        return findBookings("SELECT * FROM bookings");
    }

    public List<Booking> findByTraveler(int travelerId) {
        String sql = "SELECT * FROM bookings WHERE traveler_id = ?";
        List<Booking> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, travelerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapBooking(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding bookings by traveler", e);
        }
        return list;
    }

    public List<Booking> findByAgent(int agentId) {
        String sql = "SELECT b.* FROM bookings b JOIN listings l ON b.listing_id = l.id WHERE l.agent_id = ?";
        List<Booking> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, agentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(mapBooking(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding bookings by agent", e);
        }
        return list;
    }

    @Override
    public void update(Booking booking) {
        String sql = "UPDATE bookings SET status = ?, start_date = ?, end_date = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, booking.getStatus().name());
            stmt.setDate(2, Date.valueOf(booking.getStartDate()));
            stmt.setDate(3, Date.valueOf(booking.getEndDate()));
            stmt.setInt(4, booking.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating booking", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM bookings WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting booking", e);
        }
    }

    private List<Booking> findBookings(String query) {
        List<Booking> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapBooking(rs));
        } catch (SQLException e) {
            throw new DataAccessException("Error finding bookings", e);
        }
        return list;
    }

    private Booking mapBooking(ResultSet rs) throws SQLException {
        return new Booking(
            rs.getInt("id"),
            rs.getInt("traveler_id"),
            rs.getInt("listing_id"),
            BookingStatus.valueOf(rs.getString("status")),
            rs.getDate("start_date").toLocalDate(),
            rs.getDate("end_date").toLocalDate(),
            rs.getTimestamp("booked_at").toLocalDateTime()
        );
    }
}
