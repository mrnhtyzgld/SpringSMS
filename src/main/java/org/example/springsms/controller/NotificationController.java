package org.example.springsms.controller;

import org.example.springsms.dto.SendBulkDto;
import org.example.springsms.dto.SendDto;
import org.example.springsms.model.SendBulkModel;
import org.example.springsms.model.SendModel;
import org.example.springsms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    private SmsService smsService;

    @PostMapping("/send")
    public ResponseEntity<String> sendSms(@RequestBody SendDto sendDto) {
        smsService.sendSms(sendDto.getPhoneNumber(), sendDto.getMessage()); // Servis katmanına yönlendirme
        return new ResponseEntity<>("SMS sent successfully.", HttpStatus.OK);
    }

    @PostMapping("/send-bulk")
    public ResponseEntity<String> sendBulkSms(@RequestBody SendBulkDto sendBulkDto) {
        smsService.sendBulkSms(sendBulkDto.getPhoneNumbers(), sendBulkDto.getMessage()); // Servis katmanına yönlendirme
        return new ResponseEntity<>("Bulk SMS sent successfully.", HttpStatus.OK);
    }
}
