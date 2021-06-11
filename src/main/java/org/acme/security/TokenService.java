package org.acme.security;

import org.acme.model.Person;
import org.eclipse.microprofile.jwt.Claims;
import org.jboss.logmanager.Logger;
import org.jose4j.jwt.JwtClaims;

import javax.enterprise.context.RequestScoped;
import java.util.Arrays;
import java.util.UUID;

@RequestScoped
public class TokenService {

    public final static Logger LOGGER = Logger.getLogger(TokenService.class.getSimpleName());

    public String generateUserToken(Person person) {
        //hier als param een user object vragen
        //vervolgens Roles.user vervangen met user.getRole
        return generateToken(person.getEmail(), person.getRole());
    }

    public String generateToken(String email,  String... roles) {
        try {
            JwtClaims jwtClaims = new JwtClaims();
            jwtClaims.setIssuer("Faruk");
            jwtClaims.setJwtId(UUID.randomUUID().toString());
            jwtClaims.setSubject(email);
            jwtClaims.setClaim(Claims.upn.name(), email);
            jwtClaims.setClaim(Claims.preferred_username.name(), email);
            jwtClaims.setClaim(Claims.groups.name(), Arrays.asList(roles));
            jwtClaims.setAudience("using-jwt");
            jwtClaims.setExpirationTimeMinutesInTheFuture(60);

            String token = TokenUtils.generateTokenString(jwtClaims);
            LOGGER.info("TOKEN generated: " + token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}