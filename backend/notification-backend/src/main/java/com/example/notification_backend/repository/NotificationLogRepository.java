package com.example.notification_backend.repository;

import com.example.notification_backend.entity.NotificationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {

    List<NotificationLog> findByEmailSentFalse();
    
    List<NotificationLog> findByPersonelId(Long personelId);
    
    List<NotificationLog> findByOperationType(String operationType);
    
    Page<NotificationLog> findByEmailSent(Boolean emailSent, Pageable pageable);
    
    @Query("SELECT n FROM NotificationLog n WHERE n.createdAt BETWEEN :startDate AND :endDate")
    List<NotificationLog> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(n) FROM NotificationLog n WHERE n.emailSent = true")
    long countSuccessfulNotifications();
    
    @Query("SELECT COUNT(n) FROM NotificationLog n WHERE n.emailSent = false")
    long countFailedNotifications();
    
    @Query("SELECT n FROM NotificationLog n WHERE n.personelEmail = :email ORDER BY n.createdAt DESC")
    List<NotificationLog> findByPersonelEmailOrderByCreatedAtDesc(@Param("email") String email);
}
