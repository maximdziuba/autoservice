package com.auto.autoservice.telegram;

import com.auto.autoservice.repository.CarRepository;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {

    private final BotAuthorization botAuthorization;
    private final CarRepository carRepository;
    private final MessageSource messageSource;
    private final BotUtils botUtils;
    private BotState state;
    private static Map<String, String> context = new HashMap<>();

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
        switch (messageText) {
            case "/start":
                greeting(message);
                break;
            case "Додати авто":
                getCarBrand(message);
                state = BotState.GET_CAR_BRAND;
                break;
            default:
                if (message.hasText() && Objects.equals(state, BotState.GET_CAR_BRAND))
                    saveCarBrand(message);
                else if (message.hasText() && Objects.equals(state, BotState.SAVED_CAR_BRAND))
                    saveCarModel(message);
                else if (message.hasText() && Objects.equals(state, BotState.SAVED_CAR_MODEL))
                    saveCarNumber(message);
                else if (message.hasText() && Objects.equals(state, BotState.SAVED_CAR_NUMBER))
                    saveCarMileage(message);
        }
    }

    private void greeting(Message message) throws TelegramApiException {
        var sendMessage = new SendMessage();
        var messageText = "Привіт!";
        var keyboard = botUtils.createReplyMarkupKeyboard("Додати авто");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(messageText);
        sendMessage.setReplyMarkup(keyboard);
        execute(sendMessage);
    }

    // TODO: get messages from messages.properties file
    private void getCarBrand(Message message) throws TelegramApiException {
        var sendMessage = new SendMessage();
//        var messageText = messageSource.getMessage("car.brand.message", null, Locale.forLanguageTag("ua"));
        var messageText = "Введіть марку машини";
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(messageText);
        execute(sendMessage);
        context.put("getCarBrand", null);
    }

    private void saveCarBrand(Message message) throws TelegramApiException {
        var carBrand = message.getText();
        var messageText = "Введіть модель машини";
        var sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(messageText);
        context.put("carBrand", carBrand);
        state = BotState.SAVED_CAR_BRAND;
        execute(sendMessage);
    }

    private void saveCarModel(Message message) throws TelegramApiException {
        var carModel = message.getText();
        var messageText = "Введіть номер машини";
        var sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(messageText);
        context.put("carModel", carModel);
        state = BotState.SAVED_CAR_MODEL;
        execute(sendMessage);
    }

    private void saveCarNumber(Message message) throws TelegramApiException {
        var carNumber = message.getText();
        var messageText = "Введіть пробіг машини";
        var sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(messageText);
        context.put("carNumber", carNumber);
        state = BotState.SAVED_CAR_NUMBER;
        execute(sendMessage);
    }

    private void saveCarMileage(Message message) throws TelegramApiException {
        var mileage = message.getText();
        var brand = context.get("carBrand");
        var model = context.get("carModel");
        var number = context.get("carNumber");
        var messageText = String.format("Ваше авто \nМарка: %s\nМодель: %s\nНомер: %s\nПробіг: %s",
                                                    brand, model, number, mileage);
        var sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(messageText);
        context.put("carMileage", mileage);
        state = BotState.SAVED_CAR_MILEAGE;
        execute(sendMessage);
    }
}