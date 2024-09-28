package org.example.springsms.integration;

import org.example.springsms.exception.DatabaseException;
import org.example.springsms.exception.NotificationException;
import org.example.springsms.model.NotificationStatus;
import org.example.springsms.model.SendModel;
import org.example.springsms.repository.SMSRepository;
import org.example.springsms.service.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@Testcontainers
public class SmsServiceIntegration {

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

    @BeforeEach
    public void cleanDatabase() {
        smsRepository.deleteAll();  // Veritabanını her testten önce temizle
    }


    @Test
    public void testSendSingleSmsSuccess() {
        String phoneNumber = "1234567890";
        String message = "Test message";

        // Send an SMS and validate that it was sent successfully
        CompletableFuture<Void> future = smsService.sendSms(phoneNumber, message);
        try { // FIXME try cath to throw or the best practice
            future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        // Check that the SMS was saved to the repository
        SendModel savedModel = smsRepository.findAll().get(0); // Assuming only one record for simplicity
        assertThat(savedModel.getRecipientPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(savedModel.getMessage()).isEqualTo(message);
        assertThat(savedModel.getStatus()).isEqualTo(NotificationStatus.SENT);  // Validate the status is SENT
    }

    @Test
    public void testSendSingleSmsInvalidPhoneNumber() {
        String invalidPhoneNumber = "123"; // Invalid phone number
        String message = "Test message";

        CompletableFuture<Void> future = smsService.sendSms(invalidPhoneNumber, message);
        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertThat(exception.getCause() instanceof NotificationException).isEqualTo(true);
    }

    @Test
    public void testSendSingleSmsFailedToSend() throws InterruptedException, ExecutionException {
        String phoneNumber = "1234567890";
        String message = "Test message";

        doReturn(false).when(smsService).sendApi(any(SendModel.class));


        // SMS gönder ve sonucunu bekle
        CompletableFuture<Void> future = smsService.sendSms(phoneNumber, message);
        future.get();  // Asenkron işlemin tamamlanmasını bekle

        // Veritabanında kayıtlı SMS'yi bul ve durumunu kontrol et
        SendModel savedModel = smsRepository.findAll().get(0);  // Tek kayıt olduğunu varsayıyoruz
        assertThat(savedModel.getRecipientPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(savedModel.getStatus()).isEqualTo(NotificationStatus.FAILED);  // Durumun FAILED olduğunu doğrula
    }

    @Test
    public void testSendBulkSmsSuccess() {
        String[] phoneNumbers = {"1234567890", "0987654321"};
        String message = "Bulk message";

        // Send bulk SMS
        CompletableFuture<Void> future = smsService.sendBulkSms(phoneNumbers, message);
        try {
            future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        // Check that both SMS were saved to the repository
        for (int i = 0; i < phoneNumbers.length; i++) {
            SendModel savedModel = smsRepository.findAll().get(i); // Retrieve each saved model
            assertThat(savedModel.getRecipientPhoneNumber()).isEqualTo(phoneNumbers[i]);
            assertThat(savedModel.getMessage()).isEqualTo(message);
            assertThat(savedModel.getStatus()).isEqualTo(NotificationStatus.SENT);  // Validate the status is SENT
        }
    }

    @Test
    public void testSendBulkSmsInvalidPhoneNumber() {
        String[][] vals = {{"1".repeat(10), "1".repeat(10), "3".repeat(11)},
                {"2".repeat(10), "2".repeat(9), "9".repeat(10)},
                {"3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), "3".repeat(10), null},
                {"3".repeat(10), null},
                {"4".repeat(10), ""}};


        for (int a = 0; a < vals.length; a++)
        {
            String[] invalidPhoneNumbers = vals[a]; // One invalid phone number
            String message = "Bulk message";

            CompletableFuture<Void> future = smsService.sendBulkSms(invalidPhoneNumbers, message);
            ExecutionException exception = assertThrows(ExecutionException.class, future::get);
            assertThat(exception.getCause() instanceof NotificationException).isEqualTo(true);
        }
    }

    @Test
    public void testDatabaseFailureDuringSmsSave() {
        String phoneNumber = "1234567890";
        String message = "Test message";

        mongoDBContainer.stop();
        CompletableFuture<Void> future = smsService.sendSms(phoneNumber, message);
        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertThat(exception.getCause() instanceof DatabaseException).isEqualTo(true);

        mongoDBContainer.start();
    }


}
