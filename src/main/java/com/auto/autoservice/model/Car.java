package com.auto.autoservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    private String id;
    private String brand;
    private String model;
    private String number;
    private Long mileage;
    private String userId;

    public Car(String brand, String model, String number, Long mileage, String userId) {
        this.brand = brand;
        this.model = model;
        this.number = number;
        this.mileage = mileage;
        this.userId = userId;
    }
}
