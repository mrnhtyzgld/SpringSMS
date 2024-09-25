package org.example.springsms.service;

import org.example.springsms.exception.NotificationException;
import org.example.springsms.model.SendBulkModel;
import org.example.springsms.model.SendModel;
import org.example.springsms.repository.SMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Autowired
    private SMSRepository smsRepository;

    public void sendSms(SendModel sendModel) {
        if (sendModel == null) {
            throw new NotificationException("sendModel is null");
        }
        if (! isValidPhoneNumber(sendModel))
    }

    public void sendBulkSms(SendBulkModel sendBulkModel) {
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("[0-9]{10}");
    }
}
