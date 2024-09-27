package org.example.springsms.integration;

import org.example.springsms.exception.DatabaseException;
import org.example.springsms.exception.NotificationException;
import org.example.springsms.model.NotificationStatus;
import org.example.springsms.model.SendModel;
import org.example.springsms.repository.SMSRepository;
import org.example.springsms.service.SmsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class SmsServiceIntegration {

    @Autowired
    private SmsService smsService;

    @Autowired
    private SMSRepository smsRepository;

    @Test
    public void testSendSingleSmsSuccess() {
        String phoneNumber = "1234567890";
        String message = "Test message";

        // Send an SMS and validate that it was sent successfully
        smsService.sendSms(phoneNumber, message);

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

        // Expect NotificationException when an invalid phone number is passed
        assertThrows(NotificationException.class, () -> {
            smsService.sendSms(invalidPhoneNumber, message);
        });
    }

    @Test
    public void testSendSingleSmsFailedToSend() {
        String phoneNumber = "1234567890";
        String message = "Test message";

        // Mock the sendApi method to return false to simulate failure
        SmsService spyService = org.mockito.Mockito.spy(smsService);
        org.mockito.Mockito.doReturn(false).when(spyService).sendApi(org.mockito.ArgumentMatchers.any(SendModel.class));

        // Send an SMS and validate that the status is FAILED
        spyService.sendSms(phoneNumber, message);

        // Check that the SMS was saved to the repository with FAILED status
        SendModel savedModel = smsRepository.findAll().get(0); // Assuming only one record for simplicity
        assertThat(savedModel.getRecipientPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(savedModel.getStatus()).isEqualTo(NotificationStatus.FAILED);  // Validate the status is FAILED
    }

    @Test
    public void testSendBulkSmsSuccess() {
        String[] phoneNumbers = {"1234567890", "0987654321"};
        String message = "Bulk message";

        // Send bulk SMS
        smsService.sendBulkSms(phoneNumbers, message);

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
        String[] invalidPhoneNumbers = {"123", "0987654321"}; // One invalid phone number
        String message = "Bulk message";

        // Expect NotificationException when an invalid phone number is passed in bulk
        assertThrows(NotificationException.class, () -> {
            smsService.sendBulkSms(invalidPhoneNumbers, message);
        });
    }

    @Test
    public void testDatabaseFailureDuringSmsSave() {
        String phoneNumber = "1234567890";
        String message = "Test message";

        // Simulate a database failure
        SMSRepository spyRepository = org.mockito.Mockito.spy(smsRepository);
        org.mockito.Mockito.doThrow(DataAccessException.class).when(spyRepository).save(org.mockito.ArgumentMatchers.any(SendModel.class));

        // Expect DatabaseException when database fails
        assertThrows(DatabaseException.class, () -> {
            smsService.sendSms(phoneNumber, message);
        });
    }
}
