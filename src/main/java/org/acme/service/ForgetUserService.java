package org.acme.service;

import com.google.gson.Gson;
import org.acme.model.Person;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ForgetUserService {

    @Inject
    @Channel("user-to-delete")
    Emitter<String> utd_emitter;
    public void forgetUser(String username) {
        utd_emitter.send(username);
    }
}
