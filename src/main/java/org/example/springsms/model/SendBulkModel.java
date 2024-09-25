package org.example.springsms.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class SendBulkModel {

    private String id;
    private String recipientPhoneNumber;
    private SendModel[] sendModels;
    private String status;
    private LocalDateTime timestamp;


    private SendBulkModel(Builder builder) {
        this.id = builder.id;
        this.recipientPhoneNumber = builder.recipientPhoneNumber;
        this.sendModels = builder.sendModels;
        this.status = builder.status;
        this.timestamp = builder.timestamp;
    }

    public static class Builder {
        private String id;
        private String recipientPhoneNumber;
        private SendModel[] sendModels;
        private String status;
        private LocalDateTime timestamp;


        public void id(String id) {
            this.id = id;
        }

        public void recipientPhoneNumber(String recipientPhoneNumber) {
            this.recipientPhoneNumber = recipientPhoneNumber;
        }

        public void messages(String[] messages) {
            this.sendModels = new SendModel[messages.length];
            for (int a = 0; a < messages.length; a++) {
                this.sendModels[a] = new SendModel.Builder().message(messages[a]).build();
                this
            }
            this.sendModels = message;
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

    public ArrayList<SendModel> getMessages() {
        return sendModels;
    }
    public SendModel getMessage(int index) {
        return sendModels.get(index);
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

    public void setSendModels(SendModel[] sendModels) {
        this.sendModels = sendModels;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
