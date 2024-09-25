package org.example.springsms.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class SendBulkModel {

    private String id;
    private String recipientPhoneNumber;
    private ArrayList<String> message;
    private String status;
    private LocalDateTime timestamp;


    private SendBulkModel(Builder builder) {
        this.id = builder.id;
        this.recipientPhoneNumber = builder.recipientPhoneNumber;
        this.message = builder.message;
        this.status = builder.status;
        this.timestamp = builder.timestamp;
    }

    public static class Builder {
        private String id;
        private String recipientPhoneNumber;
        private ArrayList<String> message;
        private String status;
        private LocalDateTime timestamp;


        public void id(String id) {
            this.id = id;
        }

        public void recipientPhoneNumber(String recipientPhoneNumber) {
            this.recipientPhoneNumber = recipientPhoneNumber;
        }

        public void message(ArrayList<String> message) {
            this.message = message;
        }

        public void status(String status) {
            this.status = status;
        }

        public void timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public SendBulkModel build() {
            // FIXME error handling

            return new SendBulkModel(this);
        }
    }

    public String getId() {
        return id;
    }

    public String getRecipientPhoneNumber() {
        return recipientPhoneNumber;
    }

    public ArrayList<String> getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
