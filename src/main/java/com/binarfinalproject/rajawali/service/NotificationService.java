package com.binarfinalproject.rajawali.service;

import java.util.UUID;

import com.binarfinalproject.rajawali.dto.notification.response.ResUserNotificationDto;
import com.binarfinalproject.rajawali.exception.ApiException;

public interface NotificationService {
    public ResUserNotificationDto getUserNotifications(UUID userId) throws ApiException;

    public ResUserNotificationDto readUserNotifications(UUID userId) throws ApiException;
}
