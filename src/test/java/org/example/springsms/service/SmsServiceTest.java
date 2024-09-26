package org.example.springsms.service;


import org.example.springsms.exception.NotificationException;
import org.example.springsms.model.NotificationStatus;
import org.example.springsms.model.SendBulkModel;
import org.example.springsms.model.SendModel;
import org.example.springsms.repository.SMSRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SmsServiceTest {

    public static final String msgErr = "message is invalid.";
    public static final String numErr = "recipientPhoneNumber is invalid.";

    @InjectMocks
    private SmsService smsService;

    @Mock
    private SMSRepository smsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendSuccessfully() {
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
    void testSendMessageFailure() {
        // Arrange

        String phoneNumber = "1".repeat(10);
        String message = "";

        NotificationException exception = assertThrows(NotificationException.class, () -> {
            smsService.sendSms(phoneNumber, message);
        });

        assertEquals(exception.getMessage(), msgErr);
    }

    @Test
    void testSendMessageFailure2() {
        // Arrange

        String phoneNumber = "1".repeat(10);
        String message = "a".repeat(200);

        NotificationException exception = assertThrows(NotificationException.class, () -> {
            smsService.sendSms(phoneNumber, message);
        });

        assertEquals(exception.getMessage(), msgErr);
    }
    @Test
    void testSendMessageFailure3() {
        // Arrange

        String phoneNumber = "1".repeat(10);
        String message = null;

        NotificationException exception = assertThrows(NotificationException.class, () -> {
            smsService.sendSms(phoneNumber, message);
        });

        assertEquals(exception.getMessage(), msgErr);
    }

    @Test
    void testSendBulkMessageFail() {
        String[] faultyMsgs = {"", null, "a".repeat(200)};
        for (int a = 0; a < faultyMsgs.length; a++) {
            String phoneNumber = "1".repeat(10);
            String[] phoneNumbers = new String[10];
            Arrays.fill(phoneNumbers, phoneNumber);
            String message = faultyMsgs[a];

            NotificationException exception = assertThrows(NotificationException.class, () -> {
                smsService.sendBulkSms(phoneNumbers, message);
            });

            assertEquals(exception.getMessage(), msgErr);
            System.out.println("bulk message test passed!");
        }
    }

    @Test
    void testSendBulkPhoneNumFail() {
        // Arrange

        String[] faultyPhoneNums = {null, "", "1".repeat(9), "1".repeat(11)};
        for (int b = 0; b < faultyPhoneNums.length; b++) {
            int phoneNumCount = 10;
            for (int a = 0; a < phoneNumCount; a++) {
                String phoneNumber = "1".repeat(10);
                String[] phoneNumbers = new String[10];

                Arrays.fill(phoneNumbers, phoneNumber);
                phoneNumbers[a] = "";
                String message = "sknfklsdmfklsfmdklsfm";

                NotificationException exception = assertThrows(NotificationException.class, () -> {
                    smsService.sendBulkSms(phoneNumbers, message);
                });

                assertEquals(exception.getMessage(), numErr);
                System.out.println("bulk phone number test passed!");
            }
        }

    }

    @Test
    void testSendBulkSuccesfully() {

        int phoneNumberCount = new Random().nextInt(100);
        for (int a = 0; a < phoneNumberCount; a++) {
            String message = "a".repeat(new Random().nextInt(50)+1);
            String phoneNumber = createRightPhoneNumber();
            smsService.sendSms(phoneNumber, message);



            // Assert
            ArgumentCaptor<SendModel> smsCaptor = ArgumentCaptor.forClass(SendModel.class);
            verify(smsRepository, times(a+1)).save(smsCaptor.capture());

            SendModel capturedSms = smsCaptor.getValue();
            assertEquals(phoneNumber, capturedSms.getRecipientPhoneNumber());
            assertEquals(message, capturedSms.getMessage());
            System.out.println("succesfull!");
        }
    }
    /*
    @Test
    void testSendBulkFailure() {

    }
    */


    private String createRightPhoneNumber() {
        StringBuilder str = new StringBuilder();
        Random rn = new Random();
        for (int a = 0; a < 10; a++) {
            str.append(rn.nextInt(10));
        }
        return str.toString();
    }

    private String createWrongPhoneNumber1() {
        StringBuilder str = new StringBuilder();
        Random rn = new Random();
        int[] choices = {0,5,9,11};
        int choice = choices[rn.nextInt(choices.length)];
        StringBuilder phoneNum = new StringBuilder();
        for (int a = 0; a < choice; a++)
        {
            phoneNum.append(rn.nextInt(10));
        }
        return str.toString();
    }
    private String createWrongPhoneNumber() {
        StringBuilder str = new StringBuilder();
        Random rn = new Random();
        StringBuilder phoneNum = new StringBuilder();
        for (int a = 0; a < 10; a++)
        {
            phoneNum.append(rn.nextInt(10));
        }
        return str.toString();
    }

    @Test
    void testSendSuccesfully() {


        String phoneNumber = Integer.toString(new Random().nextInt(10)).repeat(10);
        String message = "a".repeat(new Random().nextInt(50)+1);
        smsService.sendSms(phoneNumber, message);

        // Assert
        ArgumentCaptor<SendModel> smsCaptor = ArgumentCaptor.forClass(SendModel.class);
        verify(smsRepository, times(1)).save(smsCaptor.capture());

        SendModel capturedSms = smsCaptor.getValue();
        assertEquals(phoneNumber, capturedSms.getRecipientPhoneNumber());
        assertEquals(message, capturedSms.getMessage());
    }



/*
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

 */
}
