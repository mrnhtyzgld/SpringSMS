package org.example.springsms.service;

import org.example.springsms.exception.NotificationException;
import org.example.springsms.model.NotificationStatus;
import org.example.springsms.model.SendBulkModel;
import org.example.springsms.model.SendModel;
import org.example.springsms.repository.SMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SmsService {

    public static final int MAX_MSG_LENGTH = 60;
    public static final int MAX_ATTEMPTS = 5;

    @Autowired
    private SMSRepository smsRepository;

    public void sendSms(String phoneNumber, String message) {
        SendModel sendModel = new SendModel.Builder()
                .recipientPhoneNumber(phoneNumber)
                .message(message)
                .status(NotificationStatus.PENDING)
                .timestamp(LocalDateTime.now())
                .build();



        if (! isValidPhoneNumber(sendModel.getRecipientPhoneNumber())) {
            throw new NotificationException("recipient phone number is invalid");
        }
        if (! isValidMessage(sendModel.getMessage())) {
            throw new NotificationException("message is invalid");
        }

        boolean succeess = sendApi(sendModel);

        if (succeess) {
            sendModel.setStatus(NotificationStatus.SENT);
        }
        else {
            sendModel.setStatus(NotificationStatus.FAILED);
        }

        try {
            trySavingToRepository(sendModel,MAX_ATTEMPTS);
        } catch (DataAccessException e) {
            throw new NotificationException("Failed to save SMS to the database"); // TODO change the explanation
        }

    }

    public void sendBulkSms(String[] phoneNumbers, String message) {
        SendBulkModel sendBulkModel = new SendBulkModel.Builder()
                .recipientPhoneNumbers(phoneNumbers)
                .message(message)
                .status(NotificationStatus.PENDING)
                .timestamp(LocalDateTime.now())
                .build();

        if (! isValidSendBulkModel(sendBulkModel)) {
            throw new NotificationException("sendBulkModel is is not instantiated correctly or null");  // FIXME shouldnt send to client
        }
        for (int a = 0; a < sendBulkModel.RECIPIENT_COUNT; a++) {
            if (!isValidPhoneNumber(sendBulkModel.getSendModel(a).getRecipientPhoneNumber())) {

                throw new NotificationException("recipient phone number is invalid");
            }
        }
        if (! isValidMessage(sendBulkModel.getMessage())) {
            throw new NotificationException("message is invalid");
        }

        try {
            for (int a = 0; a < sendBulkModel.RECIPIENT_COUNT; a++) {
                smsRepository.save(sendBulkModel.getSendModel(a));
            }
        } catch (DataAccessException e) {
            throw new NotificationException("Failed to save SMS to the database");
        }

    }

    private void trySavingToRepository(SendModel sendModel, int maxAttempts) {
        // TODO after async try writing this
        // TODO note that this method can fail also

        try {
            smsRepository.save(sendModel);
        } catch (DataAccessException e) {
            throw new NotificationException("Failed to save SMS to the database");
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
    private boolean isValidSendModel(SendModel sendModel) {
        if (sendModel == null) return false;
        if (sendModel.getId() == null) return false;
        if (sendModel.getStatus() == null) return false;
        if (sendModel.getTimestamp() == null) return false;

        return true;
    }

    // FIXME do i even need these?
    private boolean isValidSendBulkModel(SendBulkModel sendBulkModel) {
        if (sendBulkModel == null) return false;
        if (sendBulkModel.getId() == null) return false;
        if (sendBulkModel.getStatus() == null) return false;
        if (sendBulkModel.getTimestamp() == null) return false;
        if (sendBulkModel.getSendModels() == null || sendBulkModel.getSendModels().length == 0) return false;
        if (sendBulkModel.getMessage() == null || sendBulkModel.getMessage().isEmpty()) return false;

        return true;
    }

    private boolean sendApi(SendModel sm) {
        // api

        return (Math.random() > 0.5);
    }
}
