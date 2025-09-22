package com.example.notification_backend.dto;

import java.time.LocalDateTime;

public class PersonelNotificationDTO {

    private Long personelId;
    private String ad;
    private String soyad;
    private String email;
    private String operationType; // CREATE, UPDATE, DELETE
    private String changedFields;
    private LocalDateTime timestamp;

    // Constructors
    public PersonelNotificationDTO() {}

    public PersonelNotificationDTO(Long personelId, String ad, String soyad, String email, 
                                 String operationType, String changedFields) {
        this.personelId = personelId;
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.operationType = operationType;
        this.changedFields = changedFields;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getPersonelId() {
        return personelId;
    }

    public void setPersonelId(Long personelId) {
        this.personelId = personelId;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "PersonelNotificationDTO{" +
                "personelId=" + personelId +
                ", ad='" + ad + '\'' +
                ", soyad='" + soyad + '\'' +
                ", email='" + email + '\'' +
                ", operationType='" + operationType + '\'' +
                ", changedFields='" + changedFields + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
