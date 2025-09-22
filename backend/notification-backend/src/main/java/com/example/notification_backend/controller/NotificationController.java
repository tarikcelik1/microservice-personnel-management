package com.example.notification_backend.controller;

import com.example.notification_backend.dto.NotificationRequest;
import com.example.notification_backend.entity.NotificationLog;
import com.example.notification_backend.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification Management", description = "Notification log ve email yönetimi")
@CrossOrigin(origins = "*")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    @Operation(summary = "Doğrudan notification gönder", description = "RabbitMQ kullanmadan doğrudan email notification gönderir")
    public ResponseEntity<Map<String, String>> sendNotification(@RequestBody NotificationRequest request) {
        logger.info("POST /api/notifications/send - Doğrudan notification gönderiliyor: personelId={}", request.getPersonelId());
        
        try {
            notificationService.sendDirectNotification(request);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Notification başarıyla gönderildi");
            response.put("status", "success");
            response.put("personelId", request.getPersonelId().toString());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Notification gönderilirken hata oluştu: {}", e.getMessage());
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Notification gönderilemedi: " + e.getMessage());
            response.put("status", "error");
            
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping
    @Operation(summary = "Tüm notification loglarını getir", description = "Sistemdeki tüm notification loglarını listeler")
    public ResponseEntity<List<NotificationLog>> getAllNotifications() {
        logger.info("GET /api/notifications - Tüm notification loglar istendi");
        List<NotificationLog> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/paged")
    @Operation(summary = "Sayfalama ile notification loglarını getir", description = "Belirtilen sayfa ve boyutta notification loglarını listeler")
    public ResponseEntity<Page<NotificationLog>> getAllNotificationsPaged(
            @Parameter(description = "Sayfa numarası") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sayfa boyutu") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sıralama alanı") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sıralama yönü") @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.info("GET /api/notifications/paged - Sayfalama ile notification loglar istendi");
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<NotificationLog> notifications = notificationService.getAllNotificationsPaged(pageable);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/failed")
    @Operation(summary = "Başarısız notification loglarını getir", description = "Email gönderimi başarısız olan notification loglarını listeler")
    public ResponseEntity<List<NotificationLog>> getFailedNotifications() {
        logger.info("GET /api/notifications/failed - Başarısız notification loglar istendi");
        List<NotificationLog> failedNotifications = notificationService.getFailedNotifications();
        return ResponseEntity.ok(failedNotifications);
    }

    @GetMapping("/personel/{personelId}")
    @Operation(summary = "Personel ID'sine göre notification loglarını getir", description = "Belirtilen personel ID'sine ait notification loglarını listeler")
    public ResponseEntity<List<NotificationLog>> getNotificationsByPersonelId(
            @Parameter(description = "Personel ID'si") @PathVariable Long personelId) {
        logger.info("GET /api/notifications/personel/{} - Personel notification logları istendi", personelId);
        List<NotificationLog> notifications = notificationService.getNotificationsByPersonelId(personelId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/operation/{operationType}")
    @Operation(summary = "İşlem türüne göre notification loglarını getir", description = "Belirtilen işlem türüne ait notification loglarını listeler")
    public ResponseEntity<List<NotificationLog>> getNotificationsByOperationType(
            @Parameter(description = "İşlem türü (CREATE, UPDATE, DELETE)") @PathVariable String operationType) {
        logger.info("GET /api/notifications/operation/{} - İşlem türü notification logları istendi", operationType);
        List<NotificationLog> notifications = notificationService.getNotificationsByOperationType(operationType);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/status/{emailSent}")
    @Operation(summary = "Email durumuna göre notification loglarını getir", description = "Email gönderim durumuna göre notification loglarını listeler")
    public ResponseEntity<Page<NotificationLog>> getNotificationsByStatus(
            @Parameter(description = "Email gönderim durumu") @PathVariable Boolean emailSent,
            @Parameter(description = "Sayfa numarası") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sayfa boyutu") @RequestParam(defaultValue = "10") int size) {
        
        logger.info("GET /api/notifications/status/{} - Email durumu notification logları istendi", emailSent);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<NotificationLog> notifications = notificationService.getNotificationsByStatus(emailSent, pageable);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/retry-failed")
    @Operation(summary = "Başarısız notification'ları tekrar dene", description = "Email gönderimi başarısız olan notification'ları tekrar göndermeyi dener")
    public ResponseEntity<Map<String, String>> retryFailedNotifications() {
        logger.info("POST /api/notifications/retry-failed - Başarısız notification'lar tekrar deneniyor");
        
        try {
            notificationService.retryFailedNotifications();
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Başarısız notification'lar tekrar gönderildi");
            response.put("status", "success");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Başarısız notification'lar tekrar gönderilirken hata oluştu: {}", e.getMessage());
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Hata oluştu: " + e.getMessage());
            response.put("status", "error");
            
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/statistics")
    @Operation(summary = "Notification istatistiklerini getir", description = "Başarılı ve başarısız notification sayılarını getirir")
    public ResponseEntity<Map<String, Object>> getNotificationStatistics() {
        logger.info("GET /api/notifications/statistics - Notification istatistikleri istendi");
        
        long successfulCount = notificationService.getSuccessfulNotificationCount();
        long failedCount = notificationService.getFailedNotificationCount();
        long totalCount = successfulCount + failedCount;
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", totalCount);
        statistics.put("successful", successfulCount);
        statistics.put("failed", failedCount);
        statistics.put("successRate", totalCount > 0 ? (double) successfulCount / totalCount * 100 : 0.0);
        
        return ResponseEntity.ok(statistics);
    }
}
