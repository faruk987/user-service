package org.acme.controller;

import com.google.gson.Gson;
import org.acme.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class UserController {
    @GET
    @Path("/all")
    @Produces(MediaType.TEXT_PLAIN)
    public String getUsers() throws Exception {
        String json = new Gson().toJson(User.listAll());

        return json;
    }
}
