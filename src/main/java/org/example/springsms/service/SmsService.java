package org.example.springsms.service;

import org.example.springsms.exception.NotificationException;
import org.example.springsms.model.SendBulkModel;
import org.example.springsms.model.SendModel;
import org.example.springsms.repository.SMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    public static final int MAX_MSG_LENGTH = 60;

    @Autowired
    private SMSRepository smsRepository;

    public void sendSms(SendModel sendModel) {
        if (! isValidSendModel(sendModel)) {
            throw new NotificationException("sendModel is not instantiated correctly or null");  // FIXME shouldnt send to client
        }
        if (! isValidPhoneNumber(sendModel.getRecipientPhoneNumber())) {
            throw new NotificationException("recipient phone number is invalid");
        }
        if (! isValidMessage(sendModel.getMessage())) {
            throw new NotificationException("message is invalid");
        }
        try {
            smsRepository.save(sendModel);
        } catch (DataAccessException e) {
            throw new NotificationException("Failed to save SMS to the database");
        }
    }

    public void sendBulkSms(SendBulkModel sendBulkModel) {

        if (! isValidSendBulkModel(sendBulkModel)) {
            throw new NotificationException("sendBulkModel is is not instantiated correctly or null");  // FIXME shouldnt send to client
        }
        if (! isValidPhoneNumber(sendBulkModel.getRecipientPhoneNumber())) {
            throw new NotificationException("recipient phone number is invalid");
        }
        for (int a = 0; a < sendBulkModel.MESSAGE_COUNT; a++) {
            if (! isValidMessage(sendBulkModel.getMessage(a))) {
                throw new NotificationException("message is invalid");
            }
        }
        try {
            for (int a = 0; a < sendBulkModel.MESSAGE_COUNT; a++) {
                smsRepository.save(sendBulkModel.getSendModel(a));
            }
        } catch (DataAccessException e) {
            throw new NotificationException("Failed to save SMS to the database");
            // FIXME fail durumunda diğer smsleri de eklememe "transaction" muhabbeti
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
    private boolean isValidSendBulkModel(SendBulkModel sendBulkModel) {
        if (sendBulkModel == null) return false;
        if (sendBulkModel.getId() == null) return false;
        if (sendBulkModel.getStatus() == null) return false;
        if (sendBulkModel.getTimestamp() == null) return false;

        return true;
    }
}
