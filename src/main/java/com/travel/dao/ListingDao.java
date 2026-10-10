package com.travel.dao;

import com.travel.model.*;
import com.travel.util.DBConnection;
import com.travel.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** DAO for Listing entities. Handles polymorphism by querying details tables. */
public class ListingDao implements Repository<Listing> {

    @Override
    public void save(Listing listing) {
        String sql = "INSERT INTO listings (agent_id, type, title, price, available_count, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, listing.getAgentId());
                ListingType type = determineType(listing);
                stmt.setString(2, type.name());
                stmt.setString(3, listing.getTitle());
                stmt.setDouble(4, listing.getPrice());
                stmt.setInt(5, listing.getAvailableCount());
                stmt.setString(6, listing.getStatus().name());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) listing.setId(rs.getInt(1));
                }

                saveDetails(conn, listing, type);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error saving listing", e);
        }
    }

    private ListingType determineType(Listing listing) {
        if (listing instanceof FlightListing) return ListingType.FLIGHT;
        if (listing instanceof HotelListing) return ListingType.HOTEL;
        if (listing instanceof CarListing) return ListingType.CAR;
        throw new IllegalArgumentException("Unknown listing type");
    }

    private void saveDetails(Connection conn, Listing listing, ListingType type) throws SQLException {
        if (type == ListingType.FLIGHT) {
            FlightListing fl = (FlightListing) listing;
            String sql = "INSERT INTO flight_details (listing_id, origin, destination, departure_time) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, fl.getId());
                stmt.setString(2, fl.getOrigin());
                stmt.setString(3, fl.getDestination());
                stmt.setTimestamp(4, Timestamp.valueOf(fl.getDepartureTime()));
                stmt.executeUpdate();
            }
        } else if (type == ListingType.HOTEL) {
            HotelListing hl = (HotelListing) listing;
            String sql = "INSERT INTO hotel_details (listing_id, city) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, hl.getId());
                stmt.setString(2, hl.getCity());
                stmt.executeUpdate();
            }
        } else if (type == ListingType.CAR) {
            CarListing cl = (CarListing) listing;
            String sql = "INSERT INTO car_details (listing_id, pickup_city, car_type) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, cl.getId());
                stmt.setString(2, cl.getPickupCity());
                stmt.setString(3, cl.getCarType());
                stmt.executeUpdate();
            }
        }
    }

    @Override
    public Listing findById(int id) {
        String sql = "SELECT l.*, f.origin, f.destination, f.departure_time, h.city as h_city, c.pickup_city, c.car_type " +
                     "FROM listings l " +
                     "LEFT JOIN flight_details f ON l.id = f.listing_id " +
                     "LEFT JOIN hotel_details h ON l.id = h.listing_id " +
                     "LEFT JOIN car_details c ON l.id = c.listing_id " +
                     "WHERE l.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapListing(rs);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding listing by id", e);
        }
        return null;
    }

    @Override
    public List<Listing> findAll() {
        return findListings("SELECT l.*, f.origin, f.destination, f.departure_time, h.city as h_city, c.pickup_city, c.car_type " +
                            "FROM listings l " +
                            "LEFT JOIN flight_details f ON l.id = f.listing_id " +
                            "LEFT JOIN hotel_details h ON l.id = h.listing_id " +
                            "LEFT JOIN car_details c ON l.id = c.listing_id");
    }

    public List<Listing> findByAgent(int agentId) {
        String sql = "SELECT l.*, f.origin, f.destination, f.departure_time, h.city as h_city, c.pickup_city, c.car_type " +
                     "FROM listings l " +
                     "LEFT JOIN flight_details f ON l.id = f.listing_id " +
                     "LEFT JOIN hotel_details h ON l.id = h.listing_id " +
                     "LEFT JOIN car_details c ON l.id = c.listing_id " +
                     "WHERE l.agent_id = ?";
        List<Listing> listings = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, agentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) listings.add(mapListing(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding listings by agent", e);
        }
        return listings;
    }

    public List<Listing> findByStatus(ListingStatus status) {
        String sql = "SELECT l.*, f.origin, f.destination, f.departure_time, h.city as h_city, c.pickup_city, c.car_type " +
                     "FROM listings l " +
                     "LEFT JOIN flight_details f ON l.id = f.listing_id " +
                     "LEFT JOIN hotel_details h ON l.id = h.listing_id " +
                     "LEFT JOIN car_details c ON l.id = c.listing_id " +
                     "WHERE l.status = ?";
        List<Listing> listings = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) listings.add(mapListing(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding listings by status", e);
        }
        return listings;
    }

    public List<Listing> findByTypeAndStatus(ListingType type, ListingStatus status) {
        String sql = "SELECT l.*, f.origin, f.destination, f.departure_time, h.city as h_city, c.pickup_city, c.car_type " +
                     "FROM listings l " +
                     "LEFT JOIN flight_details f ON l.id = f.listing_id " +
                     "LEFT JOIN hotel_details h ON l.id = h.listing_id " +
                     "LEFT JOIN car_details c ON l.id = c.listing_id " +
                     "WHERE l.type = ? AND l.status = ?";
        List<Listing> listings = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type.name());
            stmt.setString(2, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) listings.add(mapListing(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding listings by type and status", e);
        }
        return listings;
    }

    public List<Listing> searchApproved(ListingType type, String keyword) {
        String kw = "%" + keyword + "%";
        String sql = "SELECT l.*, f.origin, f.destination, f.departure_time, h.city as h_city, c.pickup_city, c.car_type " +
                     "FROM listings l " +
                     "LEFT JOIN flight_details f ON l.id = f.listing_id " +
                     "LEFT JOIN hotel_details h ON l.id = h.listing_id " +
                     "LEFT JOIN car_details c ON l.id = c.listing_id " +
                     "WHERE l.type = ? AND l.status = 'APPROVED' AND (" +
                     "l.title LIKE ? OR f.origin LIKE ? OR f.destination LIKE ? OR h.city LIKE ? OR c.pickup_city LIKE ?)";
        List<Listing> listings = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type.name());
            stmt.setString(2, kw);
            stmt.setString(3, kw);
            stmt.setString(4, kw);
            stmt.setString(5, kw);
            stmt.setString(6, kw);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) listings.add(mapListing(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error searching approved listings", e);
        }
        return listings;
    }

    @Override
    public void update(Listing listing) {
        String sql = "UPDATE listings SET title = ?, price = ?, available_count = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, listing.getTitle());
            stmt.setDouble(2, listing.getPrice());
            stmt.setInt(3, listing.getAvailableCount());
            stmt.setString(4, listing.getStatus().name());
            stmt.setInt(5, listing.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating listing", e);
        }
    }

    public void updateStatus(int id, ListingStatus status) {
        String sql = "UPDATE listings SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating listing status", e);
        }
    }

    public void decreaseAvailability(int id, Connection conn) throws SQLException {
        String sql = "UPDATE listings SET available_count = available_count - 1 WHERE id = ? AND available_count > 0";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows == 0) throw new SQLException("No availability left");
        }
    }

    public void increaseAvailability(int id, Connection conn) throws SQLException {
        String sql = "UPDATE listings SET available_count = available_count + 1 WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Listing findByIdForUpdate(int id, Connection conn) throws SQLException {
        String sql = "SELECT l.*, f.origin, f.destination, f.departure_time, h.city as h_city, c.pickup_city, c.car_type " +
                     "FROM listings l " +
                     "LEFT JOIN flight_details f ON l.id = f.listing_id " +
                     "LEFT JOIN hotel_details h ON l.id = h.listing_id " +
                     "LEFT JOIN car_details c ON l.id = c.listing_id " +
                     "WHERE l.id = ? FOR UPDATE";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapListing(rs);
            }
        }
        return null;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM listings WHERE id = ?"; 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting listing", e);
        }
    }

    private List<Listing> findListings(String query) {
        List<Listing> listings = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                listings.add(mapListing(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding listings", e);
        }
        return listings;
    }

    private Listing mapListing(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int agentId = rs.getInt("agent_id");
        ListingType type = ListingType.valueOf(rs.getString("type"));
        String title = rs.getString("title");
        double price = rs.getDouble("price");
        int count = rs.getInt("available_count");
        ListingStatus status = ListingStatus.valueOf(rs.getString("status"));

        if (type == ListingType.FLIGHT) {
            return new FlightListing(id, agentId, title, price, count, status, 
                rs.getString("origin"), rs.getString("destination"), rs.getTimestamp("departure_time").toLocalDateTime());
        } else if (type == ListingType.HOTEL) {
            return new HotelListing(id, agentId, title, price, count, status, rs.getString("h_city"));
        } else if (type == ListingType.CAR) {
            return new CarListing(id, agentId, title, price, count, status, rs.getString("pickup_city"), rs.getString("car_type"));
        }
        throw new DataAccessException("Unknown listing type: " + type);
    }
}
