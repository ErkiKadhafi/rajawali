package com.binarfinalproject.rajawali.service.impl;


import com.binarfinalproject.rajawali.entity.User;
import com.binarfinalproject.rajawali.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findUserByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + username));
    if (user.getIsUsed()){
      return UserDetailsImpl.build(user);
    } else {
      throw  new UsernameNotFoundException("Registration has not been completed");
    }

  }

  @Transactional
  public User loadUserByemail(String email) throws UsernameNotFoundException {
    User user = userRepository.findUserByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

    return user;
  }

}
