package com.example.personal_backend.service;

import com.example.personal_backend.config.RabbitMQConfig;
import com.example.personal_backend.dto.PersonelNotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendPersonelNotification(PersonelNotificationDTO notification) {
        try {
            logger.info("Notification gönderiliyor: {}", notification);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PERSONEL_EXCHANGE,
                    RabbitMQConfig.PERSONEL_ROUTING_KEY,
                    notification
            );
            logger.info("Notification başarıyla gönderildi: Personel ID = {}, Operation = {}",
                    notification.getPersonelId(), notification.getOperationType());
        } catch (Exception e) {
            logger.error("Notification gönderilirken hata oluştu: {}", e.getMessage(), e);
        }
    }
}
