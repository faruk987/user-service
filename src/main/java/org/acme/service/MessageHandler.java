package org.acme.service;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.vertx.core.json.Json;
import org.acme.model.BetResult;
import org.acme.model.Person;
import org.acme.model.UserWallet;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class MessageHandler {

    @Incoming("results")
    @Blocking
    @Transactional
    public void process(Object result) {
        String jsonvalue = Json.decodeValue((String) result).toString();
        BetResult betResult = Json.decodeValue(jsonvalue, BetResult.class);

        Person person = Person.findByUsername(betResult.getUsername());
        double credit = person.getWallet() + betResult.getResult();

        try {
            System.out.println("I got this from the gamble-service: " + credit);
            person.setWallet(credit);
            System.out.println("wllet is now: " + person.getWallet());
            person.persist();
        }catch (Exception e){
            System.out.println(e);
        }


    }

    @Inject
    @Channel("wallet-info")
    Emitter<String> userwalletEmitter;

    @Incoming("request")
    @Blocking
    @Transactional
    public void respond(String result) {
        Person person = Person.findByUsername(result);
        UserWallet userWallet = new UserWallet(person.getUsername(),person.getWallet());

        String json = Json.encode(userWallet);
        userwalletEmitter.send(json);
    }

    @Incoming("inlay")
    @Blocking
    @Transactional
    public void payForInlay(Object result){
        String jsonvalue = Json.decodeValue((String) result).toString();
        UserWallet userWallet = Json.decodeValue(jsonvalue, UserWallet.class);

        System.out.println("gebruiker: " + userWallet.getUsername() + " moet " + userWallet.getWallet() + " betalen");

        Person person = Person.findByUsername(userWallet.getUsername());
        double credit = person.getWallet() - userWallet.getWallet();

        try {
            person.setWallet(credit);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
