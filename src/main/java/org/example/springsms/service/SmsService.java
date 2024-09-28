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
import java.util.concurrent.CompletableFuture;

@Service
public class SmsService {

    public static final int MAX_MSG_LENGTH = 60;
    public static final int MAX_ATTEMPTS = 5;

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


        boolean success = sendApi(sendModel);



        if (success) {
            sendModel.setStatus(NotificationStatus.SENT);
        }
        else {
            sendModel.setStatus(NotificationStatus.FAILED);
        }

        try {
            smsRepository.save(sendModel); // FIXME trySavingToRepo metodunu geçir (async mi olacak?)
        } catch (DataAccessException e) {
            throw new DatabaseException(); // TODO change the explanation
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> sendBulkSms(String[] phoneNumbers, String message) {
        // FIXME async for loops
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
            boolean success = sendApi(sendModel);
            if (success) sendModel.setStatus(NotificationStatus.SENT);
            else sendModel.setStatus(NotificationStatus.FAILED);
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
            try {
                smsRepository.save(sendModel); // FIXME trySavingToRepo metodunu geçir (async mi olacak?)
            } catch (DataAccessException e) {
                throw new DatabaseException(e.getMessage() + "\n" + e.getCause() + "\n" + e.fillInStackTrace());
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    private void trySavingToRepository(SendModel sendModel, int maxAttempts) {
        // TODO after async try writing this
        // TODO note that this method can fail also

        try {
            smsRepository.save(sendModel);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage()+"\n"+e.getCause()+"\n"+e.fillInStackTrace());
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

        return true;
        //return false;
    }
}
