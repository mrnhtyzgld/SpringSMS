package org.example.springsms.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class SendModel {
    @Id
    private String id;
    private String recipientPhoneNumber;
    private String message;
    private NotificationStatus status;
    private LocalDateTime timestamp;


    public SendModel() {}
    private SendModel(Builder builder) {
        this.id = builder.id;
        this.recipientPhoneNumber = builder.recipientPhoneNumber;
        this.message = builder.message;
        this.status = builder.status;
        this.timestamp = builder.timestamp;
    }

    public static class Builder {
        private String id;
        private String recipientPhoneNumber;
        private String message;
        private NotificationStatus status;
        private LocalDateTime timestamp;



        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder recipientPhoneNumber(String recipientPhoneNumber) {
            this.recipientPhoneNumber = recipientPhoneNumber;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder status(NotificationStatus status) {
            this.status = status;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public SendModel build() {
            return new SendModel(this);
        }
    }


    public String getId() {
        return id;
    }

    public String getRecipientPhoneNumber() {
        return recipientPhoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRecipientPhoneNumber(String recipientPhoneNumber) {
        this.recipientPhoneNumber = recipientPhoneNumber;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
