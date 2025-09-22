package com.example.notification_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Notification request for sending emails")
public class NotificationRequest {
    
    @Schema(description = "Personnel ID", example = "1")
    private Long personelId;
    
    @Schema(description = "Personnel first name", example = "Ahmet")
    private String personelAd;
    
    @Schema(description = "Personnel last name", example = "Yılmaz")
    private String personelSoyad;
    
    @Schema(description = "Personnel email address", example = "ahmet.yilmaz@example.com")
    private String personelEmail;
    
    @Schema(description = "Operation type", example = "CREATE", allowableValues = {"CREATE", "UPDATE", "DELETE"})
    private String operationType;
    
    @Schema(description = "Changed fields description", example = "Ad, Soyad değiştirildi")
    private String changedFields;
    
    @Schema(description = "Recipient email for notification", example = "admin@example.com")
    private String recipientEmail;

    // Default constructor
    public NotificationRequest() {}

    // Constructor with all fields
    public NotificationRequest(Long personelId, String personelAd, String personelSoyad, 
                             String personelEmail, String operationType, String changedFields, 
                             String recipientEmail) {
        this.personelId = personelId;
        this.personelAd = personelAd;
        this.personelSoyad = personelSoyad;
        this.personelEmail = personelEmail;
        this.operationType = operationType;
        this.changedFields = changedFields;
        this.recipientEmail = recipientEmail;
    }

    // Getters and Setters
    public Long getPersonelId() {
        return personelId;
    }

    public void setPersonelId(Long personelId) {
        this.personelId = personelId;
    }

    public String getPersonelAd() {
        return personelAd;
    }

    public void setPersonelAd(String personelAd) {
        this.personelAd = personelAd;
    }

    public String getPersonelSoyad() {
        return personelSoyad;
    }

    public void setPersonelSoyad(String personelSoyad) {
        this.personelSoyad = personelSoyad;
    }

    public String getPersonelEmail() {
        return personelEmail;
    }

    public void setPersonelEmail(String personelEmail) {
        this.personelEmail = personelEmail;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(String changedFields) {
        this.changedFields = changedFields;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    @Override
    public String toString() {
        return "NotificationRequest{" +
                "personelId=" + personelId +
                ", personelAd='" + personelAd + '\'' +
                ", personelSoyad='" + personelSoyad + '\'' +
                ", personelEmail='" + personelEmail + '\'' +
                ", operationType='" + operationType + '\'' +
                ", changedFields='" + changedFields + '\'' +
                ", recipientEmail='" + recipientEmail + '\'' +
                '}';
    }
}
