package com.auto.autoservice.telegram.handlers;

import com.auto.autoservice.model.BotUser;
import com.auto.autoservice.repository.BotUserRepository;
import com.auto.autoservice.telegram.BotUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class CommonMessageHandler {

    private final BotUserRepository botUserRepository;
    private final BotUtils botUtils;

    public SendMessage greeting(Message message) throws TelegramApiException {
        var userFromDb = botUserRepository.findById(String.valueOf(message.getChatId()));
        if (userFromDb.isEmpty()) {
            var botUser = new BotUser(String.valueOf(message.getChatId()), message.getChat().getUserName(), new ArrayList<>());
            botUserRepository.save(botUser);
        }
        var sendMessage = new SendMessage();
        var messageText = "Привіт!";
        var keyboard = botUtils.createReplyMarkupKeyboard("Додати авто", "Мої авто");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(messageText);
        sendMessage.setReplyMarkup(keyboard);
        return sendMessage;
    }
}
