package org.example.springsms.model;

import java.time.LocalDateTime;

public class SendModel {
    private String id;
    private String recipientPhoneNumber;
    private String message;
    private String status;
    private LocalDateTime timestamp;


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
        private String status;
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

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public SendModel build() {
            // FIXME error handling

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

    public String getStatus() {
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
