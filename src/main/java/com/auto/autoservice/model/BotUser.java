package com.auto.autoservice.model;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotUser {

    private String id;
    @Nullable
    private String username;
    private List<Car> cars;
}
