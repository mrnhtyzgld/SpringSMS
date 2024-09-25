package org.example.springsms.service;


import org.example.springsms.exception.NotificationException;
import org.example.springsms.model.SendModel;
import org.example.springsms.repository.SMSRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        SendModel sms = new SendModel.Builder()
                .recipientPhoneNumber("5551234567")
                .message("Hello!")
                .build();

        // Act
        smsService.sendSms(sms);

        // Assert
        verify(smsRepository, times(1)).save(sms);
        assertEquals("SENT", sms.getStatus());
    }

    @Test
    void testSendSmsInvalidPhoneNumber() {
        // Arrange
        SendModel sms = new SendModel.Builder()
                .recipientPhoneNumber("invalid-phone")
                .message("Hello!")
                .build();

        // Act & Assert
        NotificationException exception = assertThrows(NotificationException.class, () -> {
            smsService.sendSms(sms);
        });

        assertEquals("Invalid phone number format!", exception.getMessage());
        verify(smsRepository, never()).save(sms); // Veritabanına kayıt yapılmadığını kontrol et
    }

    @Test
    void testSendSmsMessageTooLong() {
        // Arrange
        String longMessage = "A".repeat(161); // 161 karakterlik uzun mesaj
        SendModel sms = new SendModel.Builder()
                .recipientPhoneNumber("5551234567")
                .message(longMessage)
                .build();

        // Act & Assert
        NotificationException exception = assertThrows(NotificationException.class, () -> {
            smsService.sendSms(sms);
        });

        assertEquals("Message exceeds the maximum length of 160 characters", exception.getMessage());
        verify(smsRepository, never()).save(sms); // Veritabanına kayıt yapılmadığını kontrol et
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
