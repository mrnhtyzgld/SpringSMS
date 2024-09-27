package org.example.springsms.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class NotificationServiceTest {
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @BeforeAll
    static void setUp() {
        mongoDBContainer.start();
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());

    }

    @Test
    public void testContainerIsRunning() {
        assert mongoDBContainer.isRunning();
    }
}