package com.auto.autoservice.telegram.handlers;

import com.auto.autoservice.model.BotUser;
import com.auto.autoservice.model.Car;
import com.auto.autoservice.repository.BotUserRepository;
import com.auto.autoservice.repository.CarRepository;
import com.auto.autoservice.telegram.Bot;
import com.auto.autoservice.telegram.BotState;
import com.auto.autoservice.telegram.BotUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CarMessageHandler {

    private final CarRepository carRepository;
    private final BotUtils botUtils;
    private final BotUserRepository botUserRepository;

    // TODO: get messages from messages.properties file
    public SendMessage getCarBrand(Message message) {
        var sendMessage = new SendMessage();
//        var messageText = messageSource.getMessage("car.brand.message", null, Locale.forLanguageTag("ua"));
        var messageText = "Введіть марку машини";
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(messageText);
        Bot.context.put("getCarBrand", null);
        return sendMessage;
    }

    public SendMessage saveCarBrand(Message message) {
        var carBrand = message.getText();
        var messageText = "Введіть модель машини";
        var sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(messageText);
        Bot.context.put("carBrand", carBrand);
        Bot.state = BotState.SAVED_CAR_BRAND;
        return sendMessage;
    }

    public SendMessage saveCarModel(Message message) {
        var carModel = message.getText();
        var messageText = "Введіть номер машини";
        var sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(messageText);
        Bot.context.put("carModel", carModel);
        Bot.state = BotState.SAVED_CAR_MODEL;
        return sendMessage;
    }

    public SendMessage saveCarNumber(Message message) {
        var carNumber = message.getText();
        var messageText = "Введіть пробіг машини у км";
        var sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(messageText);
        Bot.context.put("carNumber", carNumber);
        Bot.state = BotState.SAVED_CAR_NUMBER;
        return sendMessage;
    }

    public SendMessage saveCarMileage(Message message) {
        var userId = String.valueOf(message.getChatId());
        var sendMessage = new SendMessage();
        Car car = Car.builder()
                .brand(Bot.context.get("carBrand"))
                .model(Bot.context.get("carModel"))
                .number(Bot.context.get("carNumber"))
                .mileage(Long.parseLong(message.getText()))
                .userId(userId)
                .build();
        var messageText = String.format("Ваше авто \nМарка: %s\nМодель: %s\nНомер: %s\nПробіг: %d",
                car.getBrand(), car.getModel(), car.getNumber(), car.getMileage());
        var userFromDb = botUserRepository.findById(userId).orElseThrow();
        var usersCars = userFromDb.getCars();
        usersCars.add(car);
        userFromDb.setCars(usersCars);
        botUserRepository.save(userFromDb);
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(messageText);
        Bot.context.put("carMileage", String.valueOf(car.getMileage()));
        Bot.state = BotState.SAVED_CAR_MILEAGE;
        return sendMessage;
    }

    public SendMessage sendUsersCars(Message message) {
        var sendMessage = new SendMessage();
        var userFromDb = botUserRepository.findById(String.valueOf(message.getChatId())).orElseThrow();
        var usersCars = userFromDb.getCars();
        var keyboard = botUtils.createReplyMarkupKeyboard(usersCars);
        sendMessage.setReplyMarkup(keyboard);
        if (!usersCars.isEmpty())
            sendMessage.setText("Виберіть авто зі списку");
        else
            sendMessage.setText("У вас ще немає доданих авто");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        Bot.state = BotState.GET_USERS_CARS;
        return sendMessage;
    }

    public SendMessage sendSelectedCars(Message message) {
        var userId = String.valueOf(message.getChatId());
        var messageText = "";
        var sendMessage = new SendMessage();
        var userFromDb = botUserRepository.findById(userId).orElseThrow();
        var keyboard = botUtils.createReplyMarkupKeyboard("На головну");
        List<Car> usersCars = userFromDb.getCars();
        for (Car car : usersCars) {
            if ((car.getBrand() + " " + car.getModel()).equals(message.getText())) {
                messageText = String.format("Ваше авто \nМарка: %s\nМодель: %s\nНомер: %s\nПробіг: %d",
                        car.getBrand(), car.getModel(), car.getNumber(), car.getMileage());
            }
        }
        sendMessage.setText(messageText);
        sendMessage.setReplyMarkup(keyboard);
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        return sendMessage;
    }
}
