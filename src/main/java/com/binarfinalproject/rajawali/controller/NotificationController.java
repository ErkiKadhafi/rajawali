package com.binarfinalproject.rajawali.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.binarfinalproject.rajawali.dto.notification.response.ResUserNotificationDto;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.service.NotificationService;
import com.binarfinalproject.rajawali.util.ResponseMapper;

@Controller
@RequestMapping("/v1/notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> getUserNotifications(@PathVariable UUID userId) {
        try {
            ResUserNotificationDto response = notificationService.getUserNotifications(userId);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "User notifications has successfully fetched!",
                    response);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/user/{userId}/mark-as-seen")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> readUserNotifications(@PathVariable UUID userId) {
        try {
            ResUserNotificationDto response = notificationService.readUserNotifications(userId);
            return ResponseMapper.generateResponseSuccess(HttpStatus.OK, "User notifications has successfully read!",
                    response);
        } catch (ApiException e) {
            return ResponseMapper.generateResponseFailed(
                    e.getStatus(), e.getMessage());
        } catch (Exception e) {
            return ResponseMapper.generateResponseFailed(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
