package com.oceanview.model;

public class Reservation {

    private int reservationId;
    private String guestName;
    private String contactNumber;
    private int roomId;
    private String checkInDate;   // we’ll send as "YYYY-MM-DD"
    private String checkOutDate;  // we’ll send as "YYYY-MM-DD"
    private double totalBill;

    public Reservation() {
    }

    public Reservation(int reservationId, String guestName, String contactNumber, int roomId,
                       String checkInDate, String checkOutDate, double totalBill) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.contactNumber = contactNumber;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalBill = totalBill;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }
}