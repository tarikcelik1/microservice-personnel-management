package com.example.notification_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username:noreply@company.com}")
    private String fromEmail;

    @Value("${app.notification.hr-email:hr@company.com}")
    private String hrEmail;

    public boolean sendPersonelChangeNotification(Long personelId, String personelAd, String personelSoyad, 
                                                String personelEmail, String operationType, String changedFields) {
        try {
            logger.info("Email gönderimi başlatılıyor: Personel ID={}, Operation={}", personelId, operationType);

            String subject = createSubject(operationType, personelAd, personelSoyad);
            String content = createEmailContent(personelId, personelAd, personelSoyad, personelEmail, operationType, changedFields);

            // HTML email gönder
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(hrEmail);
            helper.setSubject(subject);
            helper.setText(content, true); // true = HTML content

            mailSender.send(message);

            logger.info("Email başarıyla gönderildi: Personel ID={}, Alıcı={}", personelId, hrEmail);
            return true;

        } catch (MessagingException e) {
            logger.error("Email gönderimi sırasında hata oluştu: Personel ID={}, Hata={}", personelId, e.getMessage(), e);
            return false;
        } catch (Exception e) {
            logger.error("Beklenmeyen hata: Personel ID={}, Hata={}", personelId, e.getMessage(), e);
            return false;
        }
    }

    public boolean sendSimpleNotification(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);
            logger.info("Basit email başarıyla gönderildi: Alıcı={}", to);
            return true;

        } catch (Exception e) {
            logger.error("Basit email gönderimi sırasında hata oluştu: Alıcı={}, Hata={}", to, e.getMessage(), e);
            return false;
        }
    }

    private String createSubject(String operationType, String ad, String soyad) {
        String fullName = ad + " " + soyad;
        switch (operationType.toUpperCase()) {
            case "CREATE":
                return "Yeni Personel Eklendi: " + fullName;
            case "UPDATE":
                return "Personel Bilgileri Güncellendi: " + fullName;
            case "DELETE":
                return "Personel Silindi: " + fullName;
            default:
                return "Personel Değişikliği: " + fullName;
        }
    }

    private String createEmailContent(Long personelId, String ad, String soyad, String email, 
                                    String operationType, String changedFields) {
        Context context = new Context();
        context.setVariable("personelId", personelId);
        context.setVariable("ad", ad);
        context.setVariable("soyad", soyad);
        context.setVariable("email", email);
        context.setVariable("operationType", operationType);
        context.setVariable("changedFields", changedFields);
        context.setVariable("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));

        try {
            return templateEngine.process("personel-notification", context);
        } catch (Exception e) {
            logger.warn("Template işlenirken hata oluştu, basit metin döndürülüyor: {}", e.getMessage());
            return createSimpleEmailContent(personelId, ad, soyad, email, operationType, changedFields);
        }
    }

    private String createSimpleEmailContent(Long personelId, String ad, String soyad, String email, 
                                          String operationType, String changedFields) {
        StringBuilder content = new StringBuilder();
        content.append("Personel Bilgileri Değişiklik Bildirimi\n\n");
        content.append("Personel ID: ").append(personelId).append("\n");
        content.append("Ad Soyad: ").append(ad).append(" ").append(soyad).append("\n");
        content.append("Email: ").append(email).append("\n");
        content.append("İşlem Türü: ").append(getOperationTypeText(operationType)).append("\n");
        
        if (changedFields != null && !changedFields.isEmpty()) {
            content.append("Değişen Alanlar: ").append(changedFields).append("\n");
        }
        
        content.append("Tarih: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))).append("\n\n");
        content.append("Bu otomatik bir bildirim mesajıdır.");
        
        return content.toString();
    }

    private String getOperationTypeText(String operationType) {
        switch (operationType.toUpperCase()) {
            case "CREATE":
                return "Yeni Personel Eklendi";
            case "UPDATE":
                return "Personel Bilgileri Güncellendi";
            case "DELETE":
                return "Personel Silindi";
            default:
                return operationType;
        }
    }
}
