package org.example.springsms.repository;

import org.example.springsms.model.SendModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SMSRepository extends MongoRepository<SendModel, String> {
}
