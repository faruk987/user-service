package org.acme.service;

import com.google.gson.Gson;
import io.quarkus.elytron.security.common.BcryptUtil;
import org.acme.model.Person;
import org.acme.model.UserJWT;
import org.acme.security.TokenService;
import org.wildfly.security.password.WildFlyElytronPasswordProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.ws.rs.NotFoundException;

import org.wildfly.security.password.Password;
import org.wildfly.security.password.PasswordFactory;
import org.wildfly.security.password.interfaces.BCryptPassword;
import org.wildfly.security.password.util.ModularCrypt;

import io.quarkus.elytron.security.common.BcryptUtil;

@ApplicationScoped
public class UserService {
    private final String DEFAULT_ROLE = "User";
    private final double DEFAULT_WALLET_CREDIT = 0;

    @Inject
    TokenService tokenService;

    @Inject
    ForgetUserService forgetUserService;

    private boolean emailExists(String email) {
        Person person = Person.findByEmail(email);
        return person != null;
    }
    private boolean userExists(String username) {
        Person person = Person.findByUsername(username);
        return person != null;
    }

    private boolean verifyBCryptPassword(String bCryptPasswordHash, String passwordToVerify) throws Exception {

        WildFlyElytronPasswordProvider provider = new WildFlyElytronPasswordProvider();

        // 1. Create a BCrypt Password Factory
        PasswordFactory passwordFactory = PasswordFactory.getInstance(BCryptPassword.ALGORITHM_BCRYPT, provider);

        // 2. Decode the hashed user password
        Password userPasswordDecoded = ModularCrypt.decode(bCryptPasswordHash);

        // 3. Translate the decoded user password object to one which is consumable by this factory.
        Password userPasswordRestored = passwordFactory.translate(userPasswordDecoded);

        // Verify existing user password you want to verify
        return passwordFactory.verify(userPasswordRestored, passwordToVerify.toCharArray());

    }

    public void storeUser(String username, String password, String email){
        if (!emailExists(email) && !userExists(username)){
            Person person = new Person();
            person.username = username;
            person.password = BcryptUtil.bcryptHash(password);
            person.role = DEFAULT_ROLE;
            person.email = email;
            person.wallet = DEFAULT_WALLET_CREDIT;

            person.persist();
        }else {
            throw new KeyAlreadyExistsException();
        }
    }

    public void updatePassword(String password, String username){
        if (userExists(username)){
            Person person = Person.findByUsername(username);
            person.setPassword(BcryptUtil.bcryptHash(password));
        }else {
            throw new KeyAlreadyExistsException();
        }
    }

    public void updateWallet(double credit, String username){
        if (userExists(username)){
            Person person = Person.findByUsername(username);
            person.setWallet(credit);
        }else {
            throw new NotFoundException();
        }
    }

    public void deleteUser(String username){
        if (userExists(username)){
            Person.delete("username", username);
            forgetUserService.forgetUser(username);
        }else {
            throw new NotFoundException();
        }
    }

    public String authenticateUser(String username, String password) throws Exception {
        if (userExists(username)){
            Person person = Person.findByUsername(username);
            if (verifyBCryptPassword(person.getPassword(),password)){
                UserJWT userJWT = new UserJWT(username, tokenService.generateUserToken(person));
                String json = new Gson().toJson(userJWT);
                return json;
            }
        }
        return null;
    }
}
