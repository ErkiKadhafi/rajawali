package com.binarfinalproject.rajawali.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.binarfinalproject.rajawali.dto.notification.response.ResNotificationDto;
import com.binarfinalproject.rajawali.dto.notification.response.ResUserNotificationDto;
import com.binarfinalproject.rajawali.entity.Notification;
import com.binarfinalproject.rajawali.entity.User;
import com.binarfinalproject.rajawali.exception.ApiException;
import com.binarfinalproject.rajawali.repository.NotificationRepository;
import com.binarfinalproject.rajawali.repository.UserRepository;
import com.binarfinalproject.rajawali.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public ResUserNotificationDto getUserNotifications(UUID userId) throws ApiException {
        Optional<User> userOnDb = userRepository.findById(userId);
        if (userOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "User with email '" + userId + "' doesn't exist");

        List<Notification> notifications = notificationRepository.findTop6ByUserIdOrderByCreatedAtDesc(userId);
        List<ResNotificationDto> resNotificationDto = notifications
                .stream()
                .map(notification -> modelMapper
                        .map(notification, ResNotificationDto.class))
                .collect(Collectors.toList());
        ResUserNotificationDto resUserNotificationDto = new ResUserNotificationDto();
        resUserNotificationDto.setIsSeen(userOnDb.get().getNotificationIsSeen());
        resUserNotificationDto.setNotifications(resNotificationDto);

        return resUserNotificationDto;
    }

    @Override
    public ResUserNotificationDto readUserNotifications(UUID userId) throws ApiException {
        Optional<User> userOnDb = userRepository.findById(userId);
        if (userOnDb.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "User with email '" + userId + "' doesn't exist");
        User user = userOnDb.get();
        user.setNotificationIsSeen(true);
        User updatedUser = userRepository.saveAndFlush(user);

        List<Notification> notifications = notificationRepository.findTop6ByUserIdOrderByCreatedAtDesc(userId);
        List<ResNotificationDto> resNotificationDto = notifications
                .stream()
                .map(notification -> modelMapper
                        .map(notification, ResNotificationDto.class))
                .collect(Collectors.toList());

        ResUserNotificationDto resUserNotificationDto = new ResUserNotificationDto();
        resUserNotificationDto.setIsSeen(updatedUser.getNotificationIsSeen());
        resUserNotificationDto.setNotifications(resNotificationDto);

        return resUserNotificationDto;

    }

}
