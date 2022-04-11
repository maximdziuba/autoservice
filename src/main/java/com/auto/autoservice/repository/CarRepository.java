package com.auto.autoservice.repository;

import com.auto.autoservice.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends MongoRepository<Car, String> {
}
