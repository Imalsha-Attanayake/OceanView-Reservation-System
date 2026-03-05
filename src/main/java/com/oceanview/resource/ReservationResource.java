package com.oceanview.resource;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import com.oceanview.dao.RoomDAO;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Path("/reservations")
public class ReservationResource {

    private ReservationDAO reservationDAO = new ReservationDAO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createReservation(Reservation reservation) {

        int reservationId = reservationDAO.addReservation(reservation);

        if (reservationId > 0) {
            return "{\"message\":\"Reservation created successfully\", \"reservationId\":" + reservationId + "}";
        } else {
            return "{\"message\":\"Reservation creation failed\"}";
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Reservation getReservationById(@PathParam("id") int id) {
        return reservationDAO.getReservationById(id);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateReservation(@PathParam("id") int id, Reservation reservation) {

        boolean updated = reservationDAO.updateReservation(id, reservation);

        if (updated) {
            return "{\"message\":\"Reservation updated successfully\", \"reservationId\":" + id + "}";
        } else {
            return "{\"message\":\"Reservation update failed or reservation not found\"}";
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteReservation(@PathParam("id") int id) {

        boolean deleted = reservationDAO.deleteReservation(id);

        if (deleted) {
            return "{\"message\":\"Reservation deleted successfully\", \"reservationId\":" + id + "}";
        } else {
            return "{\"message\":\"Reservation delete failed or reservation not found\"}";
        }
    }

    @GET
    @Path("/calculate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateBill(@QueryParam("roomId") int roomId,
                                  @QueryParam("checkIn") String checkIn,
                                  @QueryParam("checkOut") String checkOut) {

        // Validate parameters
        if (roomId <= 0 || checkIn == null || checkOut == null || checkIn.isBlank() || checkOut.isBlank()) {
            return Response.status(400)
                    .entity("{\"message\":\"roomId, checkIn and checkOut are required\"}")
                    .build();
        }

        // Validate date format + nights
        long nights;
        try {
            LocalDate in = LocalDate.parse(checkIn);
            LocalDate out = LocalDate.parse(checkOut);
            nights = ChronoUnit.DAYS.between(in, out);
        } catch (Exception ex) {
            return Response.status(400)
                    .entity("{\"message\":\"Invalid date format. Use YYYY-MM-DD\"}")
                    .build();
        }

        if (nights <= 0) {
            return Response.status(400)
                    .entity("{\"message\":\"Check-out date must be after check-in date\"}")
                    .build();
        }

        // Get room price
        RoomDAO roomDAO = new RoomDAO();
        double rate = roomDAO.getRoomPrice(roomId);

        if (rate < 0) {
            return Response.status(404)
                    .entity("{\"message\":\"Room not found\"}")
                    .build();
        }

        double total = nights * rate;

        String json = "{"
                + "\"roomId\":" + roomId + ","
                + "\"nights\":" + nights + ","
                + "\"rate\":" + rate + ","
                + "\"totalBill\":" + total
                + "}";

        return Response.ok(json).build();
    }

}
