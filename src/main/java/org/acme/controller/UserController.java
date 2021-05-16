package org.acme.controller;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

@Path("/")
public class UserController {
    @GET
    @Path("/admin")
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    public String getUsers() throws Exception {

        return "Admin";
    }

    @GET
    @Path("/me")
    @RolesAllowed("user")
    @Produces(MediaType.TEXT_PLAIN)
    public String me(@Context SecurityContext securityContext) {
        return securityContext.getUserPrincipal().getName();
    }
}
