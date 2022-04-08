package com.auto.autoservice.repository;

import com.auto.autoservice.model.BotUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BotUserRepository extends MongoRepository<BotUser, Long> {
}
