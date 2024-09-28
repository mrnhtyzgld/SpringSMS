package org.example.springsms.integration;

import org.example.springsms.model.NotificationStatus;
import org.example.springsms.model.SendModel;
import org.example.springsms.repository.SMSRepository;
import org.example.springsms.service.SmsService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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

    @Test
    public void testSendAndSendBulkConcurrently() throws InterruptedException, ExecutionException {
        String message = "Concurrent SMS Test 2";



        List<String> phoneNumbers = List.of("0".repeat(10), "1".repeat(10), "2".repeat(10), "3".repeat(10), "4".repeat(10));

        String[] bulkPhoneNumbers1 = {"5".repeat(10), "6".repeat(10), "7".repeat(10)};
        String[] bulkPhoneNumbers2 = {"8".repeat(10), "9".repeat(10), "0".repeat(10)};
        String[] bulkPhoneNumbers3 = {"1".repeat(10), "2".repeat(10), "3".repeat(10)};
        String[] bulkPhoneNumbers4 = {"4".repeat(10), "5".repeat(10), "6".repeat(10)};
        String[] bulkPhoneNumbers5 = {"7".repeat(10), "8".repeat(10), "9".repeat(10)};

        List<CompletableFuture<Void>> singleSmsFutures = phoneNumbers.stream()
                .map(phoneNumber -> smsService.sendSms(phoneNumber, message))
                .toList();

        List<CompletableFuture<Void>> bulkSmsFutures = List.of(
                smsService.sendBulkSms(bulkPhoneNumbers1, message),
                smsService.sendBulkSms(bulkPhoneNumbers2, message),
                smsService.sendBulkSms(bulkPhoneNumbers3, message),
                smsService.sendBulkSms(bulkPhoneNumbers4, message),
                smsService.sendBulkSms(bulkPhoneNumbers5, message)
        );


        List<CompletableFuture<Void>> allFutures = new ArrayList<>();
        allFutures.addAll(singleSmsFutures);
        allFutures.addAll(bulkSmsFutures);

        CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0])).join();

        List<SendModel> savedMessages = smsRepository.findAll();

        // 5 senModel + 5 sendBulkModel * 3 = 20 sendModel
        assertThat(savedMessages).hasSize(phoneNumbers.size() + (bulkPhoneNumbers1.length * bulkSmsFutures.size()));

        for (SendModel savedMessage : savedMessages) {
            assertThat(savedMessage.getMessage()).isEqualTo(message);
            assertThat(savedMessage.getStatus()).isEqualTo(NotificationStatus.SENT);  // Her bir mesajın 'SENT' olduğunu doğrula
        }

    }
}
