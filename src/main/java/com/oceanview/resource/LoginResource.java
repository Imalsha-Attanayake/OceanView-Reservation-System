package com.oceanview.resource;

import com.oceanview.dao.UserDAO;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@Path("/login")
public class LoginResource {

    private UserDAO userDAO = new UserDAO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String login(Map<String, String> credentials) {

        String username = credentials.get("username");
        String password = credentials.get("password");

        if (username == null || password == null) {
            return "{\"message\":\"Username and password are required\"}";
        }

        boolean valid = userDAO.validateUser(username, password);

        if (valid) {
            return "{\"message\":\"Login successful\"}";
        } else {
            return "{\"message\":\"Invalid username or password\"}";
        }
    }
}