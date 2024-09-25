package org.example.springsms.service;

import org.example.springsms.model.SendBulkModel;
import org.example.springsms.model.SendModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Autowired
    private SmsService smsService;

    public void sendSms(SendModel sendModel) {
    }

    public void sendBulkSms(SendBulkModel sendBulkModel) {
    }
}
