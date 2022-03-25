package com.auto.autoservice.telegram;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BotAuthorization {

    private final List<String> users = List.of("maximdziuba");

    public boolean authorize(String userId) {
        if (users.contains(userId)) {
            return true;
        }
        return false;
    }

}
