package com.auto.autoservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    private Long id;
    private String brand;
    private String model;
    private String number;
    private Long mileage;
}
