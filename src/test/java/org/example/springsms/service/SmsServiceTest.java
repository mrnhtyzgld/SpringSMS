package org.example.springsms.service;


import org.example.springsms.exception.NotificationException;
import org.example.springsms.model.SendBulkModel;
import org.example.springsms.model.SendModel;
import org.example.springsms.repository.SMSRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SmsServiceTest {

    @InjectMocks
    private SmsService smsService;

    @Mock
    private SMSRepository smsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendSmsSuccessfully() {
        // Arrange

        String phoneNumber = "1".repeat(10);
        String message = "Hello!";

        // Act
        smsService.sendSms(phoneNumber,message);

        // Assert
        ArgumentCaptor<SendModel> smsCaptor = ArgumentCaptor.forClass(SendModel.class);
        verify(smsRepository, times(1)).save(smsCaptor.capture());

        SendModel capturedSms = smsCaptor.getValue();
        assertEquals(phoneNumber, capturedSms.getRecipientPhoneNumber());
        assertEquals(message, capturedSms.getMessage());
    }

    @Test
    void testSendBulkSmsSuccessfully() {
        // Arrange

        int phoneCount = 10;
        String[] phoneNumbers = new String[phoneCount];
        for (int a = 0; a < 10; a++) {
            phoneNumbers[a] = "1".repeat(10);
        }
        String message = "Hello!";

        // Act
        smsService.sendBulkSms(phoneNumbers,message);

        // Assert
        ArgumentCaptor<SendBulkModel> smsCaptor = ArgumentCaptor.forClass(SendBulkModel.class);
        verify(smsRepository, times(1)).save(smsCaptor.capture());

        SendModel capturedSms = smsCaptor.getValue();
        assertEquals(phoneNumber, capturedSms.getRecipientPhoneNumber());
        assertEquals(message, capturedSms.getMessage());
    }


    @Test
    void testDatabaseError() {
        // Arrange
        SendModel sms = new SendModel.Builder()
                .recipientPhoneNumber("5551234567")
                .message("Hello!")
                .build();

        doThrow(new RuntimeException("Database is down")).when(smsRepository).save(sms);

        // Act & Assert
        NotificationException exception = assertThrows(NotificationException.class, () -> {
            smsService.sendSms(sms);
        });

        assertEquals("Failed to save SMS to the database", exception.getMessage());
        verify(smsRepository, times(1)).save(sms);
    }
}
