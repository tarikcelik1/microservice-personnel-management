package com.example.notification_backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_log")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "personel_id", nullable = false)
    private Long personelId;

    @Column(name = "personel_ad", length = 100)
    private String personelAd;

    @Column(name = "personel_soyad", length = 100)
    private String personelSoyad;

    @Column(name = "personel_email", length = 200)
    private String personelEmail;

    @Column(name = "operation_type", length = 50, nullable = false)
    private String operationType; // CREATE, UPDATE, DELETE

    @Column(name = "changed_fields", length = 1000)
    private String changedFields;

    @Column(name = "email_sent", nullable = false)
    private Boolean emailSent = false;

    @Column(name = "email_subject", length = 200)
    private String emailSubject;

    @Column(name = "email_content", columnDefinition = "TEXT")
    private String emailContent;

    @Column(name = "recipient_email", length = 200)
    private String recipientEmail;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    // Constructors
    public NotificationLog() {}

    public NotificationLog(Long personelId, String personelAd, String personelSoyad, String personelEmail,
                          String operationType, String changedFields) {
        this.personelId = personelId;
        this.personelAd = personelAd;
        this.personelSoyad = personelSoyad;
        this.personelEmail = personelEmail;
        this.operationType = operationType;
        this.changedFields = changedFields;
        this.emailSent = false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(Boolean emailSent) {
        this.emailSent = emailSent;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    @Override
    public String toString() {
        return "NotificationLog{" +
                "id=" + id +
                ", personelId=" + personelId +
                ", personelAd='" + personelAd + '\'' +
                ", personelSoyad='" + personelSoyad + '\'' +
                ", operationType='" + operationType + '\'' +
                ", emailSent=" + emailSent +
                ", createdAt=" + createdAt +
                '}';
    }
}
