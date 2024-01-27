package com.binarfinalproject.rajawali.security.service;

import com.binarfinalproject.rajawali.dto.Roles.response.ResRolesDto;
import com.binarfinalproject.rajawali.dto.auth.request.SignupRequest;
import com.binarfinalproject.rajawali.dto.user.ResUserDto;
import com.binarfinalproject.rajawali.entity.Role;
import com.binarfinalproject.rajawali.entity.User;
import com.binarfinalproject.rajawali.enums.ERole;
import com.binarfinalproject.rajawali.repository.RoleRepository;
import com.binarfinalproject.rajawali.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SignUpService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ModelMapper modelMapper;

    //cek username ready or not
    public boolean checkUsername(SignupRequest request){
        return userRepository.existsByUsername(request.getUsername());
    }

    //cek gmail ready or not
    public boolean checkGmail(SignupRequest request){
        return userRepository.existsByEmail(request.getEmail());
    }

    public ResUserDto createAccount(@NotNull SignupRequest signUpRequest){
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        ResUserDto response = modelMapper.map(user, ResUserDto.class);
        response.setSet_roles(roles.stream().map(data -> modelMapper.map(data, ResRolesDto.class)).collect(Collectors.toSet()));
        return response;
    }

}
