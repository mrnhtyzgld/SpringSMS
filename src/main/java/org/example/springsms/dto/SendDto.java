package org.example.springsms.dto;

import jakarta.validation.constraints.NotNull;

public class SendDto {
    @NotNull
    private String phoneNumber;
    @NotNull
    private String message;
    public SendDto() {}
    public SendDto(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
