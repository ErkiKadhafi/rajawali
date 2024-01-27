package com.binarfinalproject.rajawali.dto.user;

import com.binarfinalproject.rajawali.dto.Roles.response.ResRolesDto;
import com.binarfinalproject.rajawali.entity.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ResUserDto {
    private  String username;
    private String email;
    private String password;
    private Set<ResRolesDto> set_roles = new HashSet<>();
}
