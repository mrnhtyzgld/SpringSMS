package org.example.springsms.dto;

public class SendBulkDto {
    private String[] phoneNumbers;
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
