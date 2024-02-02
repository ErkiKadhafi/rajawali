package com.binarfinalproject.rajawali.repository;

import java.util.List;
import java.util.UUID;

import com.binarfinalproject.rajawali.entity.Notification;

public interface NotificationRepository extends SoftDeleteRepository<Notification, UUID> {
    List<Notification> findTop6ByUserIdOrderByCreatedAtDesc(UUID userId);
}
