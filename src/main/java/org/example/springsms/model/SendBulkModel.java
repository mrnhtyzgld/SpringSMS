package org.example.springsms.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class SendBulkModel {

    @Id
    private String id;
    private String message;
    private SendModel[] sendModels;
    private NotificationStatus status;
    private LocalDateTime timestamp;
    private int recipientCount;

    public void setId(String id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSendModels(SendModel[] sendModels) {
        this.sendModels = sendModels;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setRecipientCount(int recipientCount) {
        this.recipientCount = recipientCount;
    }

    public SendBulkModel() {}
    private SendBulkModel(Builder builder) {
        this.id = builder.id;
        this.message = builder.message;
        this.sendModels = builder.sendModels;
        this.status = builder.status;
        this.timestamp = builder.timestamp;
        this.recipientCount = builder.recipientCount;
    }

    public int getRecipientCount() {
        recipientCount = sendModels.length;
        return recipientCount;
    }

    public static class Builder {
        private String id;
        private String message;
        private SendModel[] sendModels;
        private NotificationStatus status;
        private LocalDateTime timestamp;
        private int recipientCount;


        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder recipientPhoneNumbers(String[] recipientPhoneNumbers) {
            this.sendModels = new SendModel[recipientPhoneNumbers.length];
            for (int a = 0; a < recipientPhoneNumbers.length; a++) {
                this.sendModels[a] = new SendModel.Builder().recipientPhoneNumber(recipientPhoneNumbers[a]).build();
            }

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

        public SendBulkModel build() {

            for (SendModel insideModel : sendModels) {
                insideModel.setMessage(message);
                insideModel.setTimestamp(timestamp);
                insideModel.setStatus(status);
                insideModel.setId(id);
            }
            
            this.recipientCount = sendModels.length;
            

            return new SendBulkModel(this);
        }
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public SendModel[] getSendModels() {
        return sendModels;
    }

    public SendModel getSendModel(int id) {
        return sendModels[id];
    }

}
