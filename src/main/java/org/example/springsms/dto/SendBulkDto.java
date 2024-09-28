package org.example.springsms.dto;

import jakarta.validation.constraints.NotNull;

public class SendBulkDto {
    @NotNull
    private String[] phoneNumbers;
    @NotNull
    private String message;
    public SendBulkDto(String[] phoneNumbers, String message) {
        this.phoneNumbers = phoneNumbers;
        this.message = message;
    }
    public String[] getPhoneNumbers() {
        return phoneNumbers;
    }
    public void setPhoneNumbers(String[] phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
