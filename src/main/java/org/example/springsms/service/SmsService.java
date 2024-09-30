package org.example.springsms.service;

import org.example.springsms.exception.DatabaseException;
import org.example.springsms.exception.NotificationException;
import org.example.springsms.model.NotificationStatus;
import org.example.springsms.model.SendBulkModel;
import org.example.springsms.model.SendModel;
import org.example.springsms.repository.SMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class SmsService {

    public static final int MAX_MSG_LENGTH = 60;
    public static final int MAX_ATTEMPTS_DB = 2;
    public static final int MAX_ATTEMPTS_API = 10;

    @Autowired
    private SMSRepository smsRepository;

    @Async
    public CompletableFuture<Void> sendSms(String phoneNumber, String message) {
        SendModel sendModel = new SendModel.Builder()
                .recipientPhoneNumber(phoneNumber)
                .message(message)
                .status(NotificationStatus.PENDING)
                .timestamp(LocalDateTime.now())
                .build();


        if (! isValidPhoneNumber(sendModel.getRecipientPhoneNumber())) {
            throw new NotificationException("recipientPhoneNumber");
        }
        if (! isValidMessage(sendModel.getMessage())) {
            throw new NotificationException("message");
        }



        int counter = 0;
        boolean success = false;
        while (!success && counter<MAX_ATTEMPTS_API) {
            success = sendApi(sendModel);
            if (success) {
                sendModel.setStatus(NotificationStatus.SENT);
            }
            else {
                sendModel.setStatus(NotificationStatus.FAILED);
            }
            counter++;
        }




        saveToRepo(sendModel, MAX_ATTEMPTS_DB);

        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> sendBulkSms(String[] phoneNumbers, String message) {
        SendBulkModel sendBulkModel = new SendBulkModel.Builder()
                .recipientPhoneNumbers(phoneNumbers)
                .message(message)
                .status(NotificationStatus.PENDING)
                .timestamp(LocalDateTime.now())
                .build();


        for (int a = 0; a < sendBulkModel.getRecipientCount(); a++) {
            if (!isValidPhoneNumber(sendBulkModel.getSendModel(a).getRecipientPhoneNumber())) {

                throw new NotificationException("recipientPhoneNumber");
            }
        }
        if (!isValidMessage(sendBulkModel.getMessage())) {
            throw new NotificationException("message");
        }

        for (SendModel sendModel : sendBulkModel.getSendModels()) {
            int counter = 0;
            boolean success = false;
            while (!success && counter<MAX_ATTEMPTS_API) {
                success = sendApi(sendModel);
                if (success)
                    sendModel.setStatus(NotificationStatus.SENT);
                else
                    sendModel.setStatus(NotificationStatus.FAILED);
                counter++;
            }
        }
        boolean isFullySaved = true;
        for (SendModel sendModel : sendBulkModel.getSendModels())
            if (sendModel.getStatus() != NotificationStatus.SENT) {
                isFullySaved = false;
                break;
            }

        if (isFullySaved) {
            sendBulkModel.setStatus(NotificationStatus.SENT);
        } else {
            sendBulkModel.setStatus(NotificationStatus.FAILED);
        }

        for (SendModel sendModel : sendBulkModel.getSendModels()) {
            saveToRepo(sendModel, MAX_ATTEMPTS_DB);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    void saveToRepo(SendModel sendModel, int maxAttempts) {
        int counter = 0;
        while (counter < maxAttempts) {
            try {
                smsRepository.save(sendModel);
                break;
            } catch (DataAccessException e) {
                if (++counter >= maxAttempts) {
                    throw new DatabaseException();
                } else {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ex) {
                        throw new DatabaseException();
                    }
                }
            }

        }

    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("[0-9]{10}");
    }
    private boolean isValidMessage(String message) {
        if (message == null) return false;
        if (message.length() > MAX_MSG_LENGTH) return false;
        if (message.isEmpty()) return false;

        return true;
    }

    public boolean sendApi(SendModel sm) {
        // api

        //return true;
        return new Random().nextBoolean();
    }
}
