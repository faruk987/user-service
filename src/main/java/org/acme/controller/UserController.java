package org.acme.controller;

import com.google.gson.Gson;
import io.quarkus.elytron.security.common.BcryptUtil;
import org.acme.model.Person;
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

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_PLAIN)
    public String login(@QueryParam("username") String username,
                        @QueryParam("password") String password) throws Exception {

        return userService.authenticateUser(username, password);
    }

    @GET
    @Path("/wallet")
    @RolesAllowed({"User", "Admin"})
    @Produces(MediaType.TEXT_PLAIN)
    public Object getWallet(@QueryParam("username") String username) {
        try {
            Person person = Person.findByUsername(username);
            return new Gson().toJson(person.getWallet());
        }catch (KeyAlreadyExistsException e){
            return Response.status(403, "Some values are not accepted");
        }
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
    @RolesAllowed({"User", "Admin"})
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
    @RolesAllowed({"User", "Admin"})
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

    @DELETE
    @Path("/delete")
    @RolesAllowed({"User", "Admin"})
    @Produces(MediaType.TEXT_PLAIN)
    public Object deleteUser(@QueryParam("username")String username){
        try{
            userService.deleteUser(username);
        }catch (Exception e){
            return Response.status(403, "Some values are not accepted");
        }
        return Response.ok();
    }
}
