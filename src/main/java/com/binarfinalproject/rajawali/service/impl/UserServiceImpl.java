package com.binarfinalproject.rajawali.service.impl;

import com.binarfinalproject.rajawali.entity.User;
import com.binarfinalproject.rajawali.repository.UserRepository;
import com.binarfinalproject.rajawali.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public User getIdUser(String email) {
        return null;
    }
}
