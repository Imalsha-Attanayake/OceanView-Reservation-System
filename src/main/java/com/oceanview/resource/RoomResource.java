package com.oceanview.resource;

import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Room;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/rooms")
public class RoomResource {

    private RoomDAO roomDAO = new RoomDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getRooms() {
        return roomDAO.getAllRooms();
    }
}