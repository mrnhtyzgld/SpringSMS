package org.example.springsms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Send a single SMS",
            description = "Sends an SMS to a specified phone number. Validates the phone number and message content before sending.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "SMS sent successfully.",
                            content = @Content(mediaType = "text/plain",
                                    examples = @ExampleObject(value = "SMS sent successfully."))),
                    @ApiResponse(responseCode = "400",
                            description = "The message for the SMS is invalid.",
                            content = @Content(mediaType = "text/plain",
                                    examples = @ExampleObject(value = "message is invalid."))),
                    @ApiResponse(responseCode = "400",
                            description = "The phone number for the SMS is invalid.",
                            content = @Content(mediaType = "text/plain",
                                    examples = @ExampleObject(value = "recipientPhoneNumber is invalid."))),
                    @ApiResponse(responseCode = "500",
                            description = "Server error.",
                            content =
                            @Content(mediaType = "text/plain",
                                    examples = @ExampleObject(value = "Internal Server Error."))),
            }
    )
    @PostMapping("/send")
    public ResponseEntity<String> sendSms(@RequestBody SendDto sendDto) {
        smsService.sendSms(sendDto.getPhoneNumber(), sendDto.getMessage()); // Servis katmanına yönlendirme
        return new ResponseEntity<>("SMS sent successfully.", HttpStatus.OK);
    }
    @Operation(
            summary = "Send bulk SMS",
            description = "Sends SMS messages to multiple phone numbers in a single request. All phone numbers and the message content are validated.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Bulk SMS sent successfully.",
                            content = @Content(mediaType = "text/plain",
                                    examples = @ExampleObject(value = "{ \"message\": \"Bulk SMS sent successfully.\" }"))),
                    @ApiResponse(responseCode = "400",
                            description = "The message for Bulk SMS is invalid.",
                            content = @Content(mediaType = "text/plain",
                                    examples = @ExampleObject(value = "{ \"error\": \"message is invalid.\" }"))),
                    @ApiResponse(responseCode = "400",
                            description = "The phone number for Bulk SMS is invalid.",
                            content = @Content(mediaType = "text/plain",
                                    examples = @ExampleObject(value = "{ \"error\": \"recipientPhoneNumber is invalid.\" }"))),
                    @ApiResponse(responseCode = "500",
                            description = "Server error.",
                            content = @Content(mediaType = "text/plain",
                                    examples = @ExampleObject(value = "{ \"error\": \"Internal server error.\" }")))
            }
    )
    @PostMapping("/send-bulk")
    public ResponseEntity<String> sendBulkSms(@RequestBody SendBulkDto sendBulkDto) {
        smsService.sendBulkSms(sendBulkDto.getPhoneNumbers(), sendBulkDto.getMessage()); // Servis katmanına yönlendirme
        return new ResponseEntity<>("Bulk SMS sent successfully.", HttpStatus.OK);
    }
}
