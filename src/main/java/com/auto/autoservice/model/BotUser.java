package com.auto.autoservice.model;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotUser {

    private Long id;
    @Nullable
    private String username;
    private Long userId;
    @Nullable
    private String phoneNumber;
}
