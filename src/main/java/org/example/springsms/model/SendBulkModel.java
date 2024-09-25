package org.example.springsms.model;

import java.time.LocalDateTime;

public class SendBulkModel {

    private String id;
    private String recipientPhoneNumber;
    private SendModel[] sendModels;
    private String status;
    private LocalDateTime timestamp;
    public final int MESSAGE_COUNT;


    private SendBulkModel(Builder builder) {
        this.id = builder.id;
        this.recipientPhoneNumber = builder.recipientPhoneNumber;
        this.sendModels = builder.sendModels;
        this.status = builder.status;
        this.timestamp = builder.timestamp;
        this.MESSAGE_COUNT = builder.messageCount;
    }

    public static class Builder {
        private String id;
        private String recipientPhoneNumber;
        private SendModel[] sendModels;
        private String status;
        private LocalDateTime timestamp;
        private int messageCount;


        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder recipientPhoneNumber(String recipientPhoneNumber) {
            this.recipientPhoneNumber = recipientPhoneNumber;
            return this;
        }

        public Builder messages(String[] messages) {
            this.sendModels = new SendModel[messages.length];
            for (int a = 0; a < messages.length; a++) {
                this.sendModels[a] = new SendModel.Builder().message(messages[a]).build();
            }
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

        public SendBulkModel build() {
            // FIXME error handling
            
            for (SendModel insideModel : sendModels) {
                insideModel.setTimestamp(timestamp);
                insideModel.setStatus(status);
                insideModel.setRecipientPhoneNumber(recipientPhoneNumber);
                insideModel.setId(id);
            }
            
            this.messageCount = sendModels.length;
            

            return new SendBulkModel(this);
        }
    }

    public String getId() {
        return id;
    }

    public String getRecipientPhoneNumber() {
        return recipientPhoneNumber;
    }

    public SendModel[] getSendModels() {
        return sendModels;
    }
    public SendModel getSendModel(int index) {
        return sendModels[index];
    }
    public String getMessage(int index) {
        return sendModels[index].getMessage();
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
