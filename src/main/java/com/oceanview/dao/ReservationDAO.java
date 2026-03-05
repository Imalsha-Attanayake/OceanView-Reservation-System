package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReservationDAO {

    // Insert reservation and return generated reservation_id
    public int addReservation(Reservation reservation) {

        String sql = "INSERT INTO reservations (guest_name, contact_number, room_id, check_in_date, check_out_date, total_bill) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, reservation.getGuestName());
            stmt.setString(2, reservation.getContactNumber());
            stmt.setInt(3, reservation.getRoomId());
            stmt.setString(4, reservation.getCheckInDate());
            stmt.setString(5, reservation.getCheckOutDate());
            stmt.setDouble(6, reservation.getTotalBill());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // reservation_id
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public Reservation getReservationById(int reservationId) {

        String sql = "SELECT reservation_id, guest_name, contact_number, room_id, " +
                "DATE_FORMAT(check_in_date, '%Y-%m-%d') AS check_in_date, " +
                "DATE_FORMAT(check_out_date, '%Y-%m-%d') AS check_out_date, " +
                "total_bill " +
                "FROM reservations WHERE reservation_id = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, reservationId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Reservation(
                        rs.getInt("reservation_id"),
                        rs.getString("guest_name"),
                        rs.getString("contact_number"),
                        rs.getInt("room_id"),
                        rs.getString("check_in_date"),
                        rs.getString("check_out_date"),
                        rs.getDouble("total_bill")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateReservation(int reservationId, Reservation reservation) {

        String sql = "UPDATE reservations SET guest_name=?, contact_number=?, room_id=?, check_in_date=?, check_out_date=?, total_bill=? " +
                "WHERE reservation_id=?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, reservation.getGuestName());
            stmt.setString(2, reservation.getContactNumber());
            stmt.setInt(3, reservation.getRoomId());
            stmt.setString(4, reservation.getCheckInDate());
            stmt.setString(5, reservation.getCheckOutDate());
            stmt.setDouble(6, reservation.getTotalBill());
            stmt.setInt(7, reservationId);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteReservation(int reservationId) {

        String sql = "DELETE FROM reservations WHERE reservation_id=?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, reservationId);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}