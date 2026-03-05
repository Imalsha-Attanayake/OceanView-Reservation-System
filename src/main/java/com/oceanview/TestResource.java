package com.oceanview;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/test")
public class TestResource {

    @GET
    public String testAPI() {
        return "OceanView Booking API is running!";
    }
}