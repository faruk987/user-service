package org.acme.controller;

import io.quarkus.elytron.security.common.BcryptUtil;
import org.acme.service.UserService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/user")
public class UserController {
    @Inject
    UserService userService;

 /*   @GET
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
    }*/

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_PLAIN)
    public String login(@QueryParam("username") String username,
                        @QueryParam("password") String password) throws Exception {

        return userService.authenticateUser(username, password);
    }

    @POST
    @Transactional
    @Path("/new")
    @Produces(MediaType.TEXT_PLAIN)
    public Object storeUser(@QueryParam("username") String username,
                            @QueryParam("password") String password,
                            @QueryParam("email") String email) {
        try {
            userService.storeUser(username,password,email);
        }catch (KeyAlreadyExistsException e){
            return Response.status(403, "Some values are not accepted");
        }
        return Response.ok();
    }

    @PUT
    @Path("/update/password")
    @Produces(MediaType.TEXT_PLAIN)
    public Object updatePassword(@QueryParam("password") String password,
                            @QueryParam("username") String username) {
        try {
            userService.updatePassword(password,username);
            return Response.ok();
        }catch (KeyAlreadyExistsException e){
            return Response.status(403, "Some values are not accepted");
        }
    }

    @PUT
    @Path("/update/wallet")
    @Produces(MediaType.TEXT_PLAIN)
    public Object updateWallet(@QueryParam("credit") double credit,
                                 @QueryParam("email") String email) {
        try {
            userService.updateWallet(credit,email);
        }catch (KeyAlreadyExistsException e){
            return Response.status(403, "Some values are not accepted");
        }
        return Response.ok();
    }
}
