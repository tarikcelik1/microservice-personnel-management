package com.example.notification_backend.service;

import com.example.notification_backend.dto.NotificationRequest;
import com.example.notification_backend.dto.PersonelNotificationDTO;
import com.example.notification_backend.entity.NotificationLog;
import com.example.notification_backend.repository.NotificationLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Bildirim işlemlerini yöneten servis sınıfı
 * Personel değişikliklerini email ile bildirme ve log tutma işlemlerini gerçekleştirir
 */
@Service
@Transactional // Sınıf seviyesinde transaction yönetimi
public class NotificationService {

    // Loglama için kullanılan logger instance'ı
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    // Bildirim loglarını kaydetmek için repository
    @Autowired
    private NotificationLogRepository notificationLogRepository;

    // Email gönderme işlemleri için servis
    @Autowired
    private EmailService emailService;

    /**
     * Personel değişiklik bildirimini işler
     * Email gönderir ve sonucu log olarak kaydeder
     * @param notification Personel değişiklik bilgisi
     */
    public void processPersonelNotification(PersonelNotificationDTO notification) {
        logger.info("Personel notification işleniyor: {}", notification);

        // Bildirim log kaydı oluştur
        NotificationLog notificationLog = new NotificationLog(
                notification.getPersonelId(),
                notification.getAd(),
                notification.getSoyad(),
                notification.getEmail(),
                notification.getOperationType(),
                notification.getChangedFields()
        );

        try {
            // Email gönderme işlemi
            boolean emailSent = emailService.sendPersonelChangeNotification(
                    notification.getPersonelId(),
                    notification.getAd(),
                    notification.getSoyad(),
                    notification.getEmail(),
                    notification.getOperationType(),
                    notification.getChangedFields()
            );

            // Log kaydını email gönderim sonucuna göre güncelle
            notificationLog.setEmailSent(emailSent);
            
            if (emailSent) {
                // Başarılı gönderim durumu
                notificationLog.setSentAt(LocalDateTime.now());
                notificationLog.setEmailSubject(createSubject(notification.getOperationType(), 
                                                            notification.getAd(), notification.getSoyad()));
                notificationLog.setRecipientEmail("hr@company.com"); // HR departmanına gönder
                logger.info("Email başarıyla gönderildi ve log kaydedildi: Personel ID={}", 
                           notification.getPersonelId());
            } else {
                // Başarısız gönderim durumu
                notificationLog.setErrorMessage("Email gönderimi başarısız");
                logger.error("Email gönderimi başarısız: Personel ID={}", notification.getPersonelId());
            }

        } catch (Exception e) {
            // Hata durumunda log kaydını güncelle
            notificationLog.setEmailSent(false);
            notificationLog.setErrorMessage("Hata: " + e.getMessage());
            logger.error("Notification işlenirken hata oluştu: Personel ID={}, Hata={}", 
                        notification.getPersonelId(), e.getMessage(), e);
        } finally {
            // Her durumda log kaydını veritabanına kaydet
            notificationLogRepository.save(notificationLog);
        }
    }

    /**
     * Doğrudan bildirim gönderir (API endpoint'ten çağrılır)
     * @param request Bildirim gönderme isteği
     * @throws RuntimeException Email gönderilemezse
     */
    public void sendDirectNotification(NotificationRequest request) {
        logger.info("Doğrudan notification gönderiliyor: {}", request);

        // Bildirim log kaydı oluştur
        NotificationLog notificationLog = new NotificationLog(
                request.getPersonelId(),
                request.getPersonelAd(),
                request.getPersonelSoyad(),
                request.getPersonelEmail(),
                request.getOperationType(),
                request.getChangedFields()
        );

        // Alıcı email adresini belirle (özel belirtilmişse onu, yoksa HR email'ini kullan)
        String recipientEmail = request.getRecipientEmail() != null ? 
                               request.getRecipientEmail() : "hr@company.com";

        try {
            // Email gönderme işlemi
            boolean emailSent = emailService.sendPersonelChangeNotification(
                    request.getPersonelId(),
                    request.getPersonelAd(),
                    request.getPersonelSoyad(),
                    request.getPersonelEmail(),
                    request.getOperationType(),
                    request.getChangedFields()
            );

            // Log kaydını güncelle
            notificationLog.setEmailSent(emailSent);
            
            if (emailSent) {
                // Başarılı gönderim
                notificationLog.setSentAt(LocalDateTime.now());
                notificationLog.setEmailSubject(createSubject(request.getOperationType(), 
                                                            request.getPersonelAd(), request.getPersonelSoyad()));
                notificationLog.setRecipientEmail(recipientEmail);
                logger.info("Email başarıyla gönderildi ve log kaydedildi: Personel ID={}", 
                           request.getPersonelId());
            } else {
                // Başarısız gönderim
                notificationLog.setErrorMessage("Email gönderimi başarısız");
                logger.error("Email gönderimi başarısız: Personel ID={}", request.getPersonelId());
            }

        } catch (Exception e) {
            // Hata durumu
            notificationLog.setEmailSent(false);
            notificationLog.setErrorMessage("Hata: " + e.getMessage());
            logger.error("Doğrudan notification gönderilirken hata oluştu: Personel ID={}, Hata={}", 
                        request.getPersonelId(), e.getMessage(), e);
            throw new RuntimeException("Notification gönderilemedi: " + e.getMessage(), e);
        } finally {
            // Log kaydını kaydet
            notificationLogRepository.save(notificationLog);
        }
    }

    /**
     * Tüm bildirimleri listeler
     * @return List<NotificationLog> - Tüm bildirim logları
     */
    @Transactional(readOnly = true)
    public List<NotificationLog> getAllNotifications() {
        return notificationLogRepository.findAll();
    }

    /**
     * Sayfalama ile bildirimleri listeler
     * @param pageable Sayfalama bilgileri
     * @return Page<NotificationLog> - Sayfalanmış bildirim logları
     */
    @Transactional(readOnly = true)
    public Page<NotificationLog> getAllNotificationsPaged(Pageable pageable) {
        return notificationLogRepository.findAll(pageable);
    }

    /**
     * Başarısız olan bildirimleri listeler
     * @return List<NotificationLog> - Başarısız bildirim logları
     */
    @Transactional(readOnly = true)
    public List<NotificationLog> getFailedNotifications() {
        return notificationLogRepository.findByEmailSentFalse();
    }

    /**
     * Belirtilen personele ait bildirimleri listeler
     * @param personelId Personel ID'si
     * @return List<NotificationLog> - Personele ait bildirim logları
     */
    @Transactional(readOnly = true)
    public List<NotificationLog> getNotificationsByPersonelId(Long personelId) {
        return notificationLogRepository.findByPersonelId(personelId);
    }

    /**
     * Belirtilen operasyon tipine ait bildirimleri listeler
     * @param operationType Operasyon tipi (CREATE, UPDATE, DELETE)
     * @return List<NotificationLog> - Operasyon tipine ait bildirim logları
     */
    @Transactional(readOnly = true)
    public List<NotificationLog> getNotificationsByOperationType(String operationType) {
        return notificationLogRepository.findByOperationType(operationType);
    }

    /**
     * Email gönderim durumuna göre bildirimleri sayfalı olarak listeler
     * @param emailSent Email gönderim durumu
     * @param pageable Sayfalama bilgileri
     * @return Page<NotificationLog> - Gönderim durumuna göre bildirim logları
     */
    @Transactional(readOnly = true)
    public Page<NotificationLog> getNotificationsByStatus(Boolean emailSent, Pageable pageable) {
        return notificationLogRepository.findByEmailSent(emailSent, pageable);
    }

    /**
     * Başarısız olan bildirimleri tekrar göndermeyi dener
     * Toplu işlem olarak çalışır
     */
    public void retryFailedNotifications() {
        logger.info("Başarısız olan notification'lar tekrar deneniyor...");
        
        // Başarısız bildirimleri bul
        List<NotificationLog> failedNotifications = notificationLogRepository.findByEmailSentFalse();
        
        // Her başarısız bildirim için tekrar deneme yap
        for (NotificationLog log : failedNotifications) {
            try {
                // Email göndermeyi tekrar dene
                boolean emailSent = emailService.sendPersonelChangeNotification(
                        log.getPersonelId(),
                        log.getPersonelAd(),
                        log.getPersonelSoyad(),
                        log.getPersonelEmail(),
                        log.getOperationType(),
                        log.getChangedFields()
                );

                // Başarılı ise log kaydını güncelle
                if (emailSent) {
                    log.setEmailSent(true);
                    log.setSentAt(LocalDateTime.now());
                    log.setErrorMessage(null); // Hata mesajını temizle
                    notificationLogRepository.save(log);
                    logger.info("Başarısız notification tekrar gönderildi: ID={}", log.getId());
                }

            } catch (Exception e) {
                logger.error("Notification tekrar gönderilirken hata oluştu: ID={}, Hata={}", 
                            log.getId(), e.getMessage());
            }
        }
    }

    /**
     * Başarılı gönderilen bildirim sayısını döner
     * @return long - Başarılı bildirim sayısı
     */
    @Transactional(readOnly = true)
    public long getSuccessfulNotificationCount() {
        return notificationLogRepository.countSuccessfulNotifications();
    }

    /**
     * Başarısız olan bildirim sayısını döner
     * @return long - Başarısız bildirim sayısı
     */
    @Transactional(readOnly = true)
    public long getFailedNotificationCount() {
        return notificationLogRepository.countFailedNotifications();
    }

    /**
     * Operasyon tipine ve personel adına göre email konu başlığı oluşturur
     * @param operationType Operasyon tipi
     * @param ad Personel adı
     * @param soyad Personel soyadı
     * @return String - Email konu başlığı
     */
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
}
