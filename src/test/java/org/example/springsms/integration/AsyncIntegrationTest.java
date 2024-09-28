package org.example.springsms.integration;

import org.example.springsms.model.NotificationStatus;
import org.example.springsms.model.SendModel;
import org.example.springsms.repository.SMSRepository;
import org.example.springsms.service.SmsService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
@Testcontainers
public class AsyncIntegrationTest {

    @Container
    private static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @SpyBean
    private SmsService smsService;

    @Autowired
    private SMSRepository smsRepository;

    @BeforeAll
    public static void startMongoDB() {
        mongoDBContainer.start();
    }

    @BeforeEach
    public void resetDB() {
        smsRepository.deleteAll();
    }

    @Test
    public void testMultipleConcurrentRequests() throws InterruptedException, ExecutionException {
        String message = "Concurrent SMS Test";

        List<String> phoneNumbers = List.of("0".repeat(10), "1".repeat(10), "2".repeat(10), "3".repeat(10), "4".repeat(10));

        List<CompletableFuture<Void>> futures = phoneNumbers.stream()
                .map(phoneNumber -> smsService.sendSms(phoneNumber, message))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        List<SendModel> savedMessages = smsRepository.findAll();
        assertThat(savedMessages).hasSize(phoneNumbers.size());

        for (SendModel savedMessage : savedMessages) {
            assertThat(savedMessage.getMessage()).isEqualTo(message);
            assertThat(savedMessage.getStatus()).isEqualTo(NotificationStatus.SENT);
        }
    }
}
