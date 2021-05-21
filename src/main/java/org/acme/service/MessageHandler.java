package org.acme.service;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import org.acme.model.BetResult;
import org.acme.model.Person;
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

        Person person = Person.findByEmail(betResult.getUserEmail());
        double credit = person.getWallet() + betResult.getResult();

        try {
            person.setWallet(credit);
        }catch (Exception e){
            System.out.println(e);
        }

        System.out.println("I got this from the gamble-service: " + credit);
    }
}
