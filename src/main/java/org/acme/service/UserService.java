package org.acme.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import org.acme.model.Person;

import javax.enterprise.context.ApplicationScoped;
import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.List;

@ApplicationScoped
public class UserService {
    private final String DEFAULT_ROLE = "User";
    private final double DEFAULT_WALLET_CREDIT = 0;

    private boolean emailExists(String email) {
        Person person = Person.findByEmail(email);
        return person != null;
    }
    private boolean userExists(String username) {
        Person person = Person.findByEmail(username);
        return person != null;
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
        if (!userExists(username)){
            Person person = Person.findByUsername(username);
            person.setPassword(BcryptUtil.bcryptHash(password));
        }else {
            throw new KeyAlreadyExistsException();
        }
    }

    public void updateWallet(double credit, String username){
        if (!userExists(username)){
            Person person = Person.findByUsername(username);
            person.setWallet(credit);
        }else {
            throw new KeyAlreadyExistsException();
        }
    }

    public void deleteUser(String username){
        if (!userExists(username)){
            Person person = Person.findByUsername(username);
            person.delete();
        }else {
            throw new KeyAlreadyExistsException();
        }
    }
}
