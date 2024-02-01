package com.binarfinalproject.rajawali.service;

import com.binarfinalproject.rajawali.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User getIdUser(String name);
}
