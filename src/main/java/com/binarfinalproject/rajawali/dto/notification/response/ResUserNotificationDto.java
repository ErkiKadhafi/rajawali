package com.binarfinalproject.rajawali.dto.notification.response;

import java.util.List;

import lombok.Data;

@Data
public class ResUserNotificationDto {
    private Boolean isSeen;

    private List<ResNotificationDto> notifications;
}
