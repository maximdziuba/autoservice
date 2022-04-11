package com.auto.autoservice.telegram;

import com.auto.autoservice.model.BotUser;
import com.auto.autoservice.repository.BotUserRepository;
import com.auto.autoservice.repository.CarRepository;
import com.auto.autoservice.telegram.handlers.CarMessageHandler;
import com.auto.autoservice.telegram.handlers.CommonMessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {

    private final BotAuthorization botAuthorization;
    private final CarMessageHandler carMessageHandler;
    private final CommonMessageHandler commonMessageHandler;
    private final CarRepository carRepository;
    private final MessageSource messageSource;
    private final BotUtils botUtils;
    private final BotUserRepository botUserRepository;
    public static BotState state;
    public static Map<String, String> context = new HashMap<>();

    @Value("${telegram.bot.username}")
    private String username;

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            Message message = update.getMessage();
            if (message != null && message.hasText()) {
                handleIncomingMessage(message);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void handleIncomingMessage(Message message) throws TelegramApiException {
        var messageText = message.getText();
        var sendMessage = new SendMessage();
        switch (messageText) {
            case "/start":
                sendMessage = commonMessageHandler.greeting(message);
                execute(sendMessage);
            case "На головну":
                sendMessage = commonMessageHandler.greeting(message);
                execute(sendMessage);
                break;
            case "Додати авто":
                sendMessage = carMessageHandler.getCarBrand(message);
                execute(sendMessage);
                state = BotState.GET_CAR_BRAND;
                break;
            case "Мої авто":
                sendMessage = carMessageHandler.sendUsersCars(message);
                execute(sendMessage);
                break;
            default:
                if (message.hasText() && Objects.equals(state, BotState.GET_CAR_BRAND)) {
                    sendMessage = carMessageHandler.saveCarBrand(message);
                    execute(sendMessage);
                } else if (message.hasText() && Objects.equals(state, BotState.SAVED_CAR_BRAND)) {
                    sendMessage = carMessageHandler.saveCarModel(message);
                    execute(sendMessage);
                } else if (message.hasText() && Objects.equals(state, BotState.SAVED_CAR_MODEL)) {
                    sendMessage = carMessageHandler.saveCarNumber(message);
                    execute(sendMessage);
                } else if (message.hasText() && Objects.equals(state, BotState.SAVED_CAR_NUMBER)) {
                    sendMessage = carMessageHandler.saveCarMileage(message);
                    execute(sendMessage);
                } else if (message.hasText() && Objects.equals(state, BotState.GET_USERS_CARS)) {
                    sendMessage = carMessageHandler.sendSelectedCars(message);
                    execute(sendMessage);
                }
        }
    }
}