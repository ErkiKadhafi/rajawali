package com.binarfinalproject.rajawali.dto.notification.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ResNotificationDto {
    private String id;

    private String notificationType;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
