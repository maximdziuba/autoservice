package com.auto.autoservice.telegram;

import com.auto.autoservice.model.Car;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
public class BotUtils {

    public ReplyKeyboardMarkup createReplyMarkupKeyboard(String... rows) {
        var keyboard = new ReplyKeyboardMarkup();
        var row = new KeyboardRow();
        for (var el : rows) {
            row.add(el);
        }
        keyboard.setKeyboard(List.of(row));
        return keyboard;
    }

    public ReplyKeyboardMarkup createReplyMarkupKeyboard(List<Car> rows) {
        var keyboard = new ReplyKeyboardMarkup();
        var row = new KeyboardRow();
        for (var el : rows) {
            var scratch = el.getBrand() + " " + el.getModel();
            row.add(scratch);
        }
        keyboard.setKeyboard(List.of(row));
        return keyboard;
    }

}
