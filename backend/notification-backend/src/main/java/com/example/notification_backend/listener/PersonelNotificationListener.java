package com.example.notification_backend.listener;

import com.example.notification_backend.config.RabbitMQConfig;
import com.example.notification_backend.dto.PersonelNotificationDTO;
import com.example.notification_backend.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonelNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(PersonelNotificationListener.class);

    @Autowired
    private NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.PERSONEL_QUEUE)
    public void handlePersonelNotification(PersonelNotificationDTO notification) {
        try {
            logger.info("RabbitMQ'dan mesaj alındı: {}", notification);
            
            // Notification'ı işle ve email gönder
            notificationService.processPersonelNotification(notification);
            
            logger.info("Personel notification başarıyla işlendi: Personel ID={}, Operation={}", 
                       notification.getPersonelId(), notification.getOperationType());
                       
        } catch (Exception e) {
            logger.error("Personel notification işlenirken hata oluştu: {}", e.getMessage(), e);
            // Bu durumda RabbitMQ mesajı tekrar kuyruğa gönderecek 
            throw e;
        }
    }
}
