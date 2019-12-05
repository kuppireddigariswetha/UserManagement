package com.tetrasoft.UserManagementWthOAuth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tetrasoft.UserManagementWthOAuth.dto.UserDto;
import com.tetrasoft.UserManagementWthOAuth.model.User;

@Service
public interface UserService {

    UserDto save(UserDto user);
    List<UserDto> findAll();
    User findOne(long id);
    void delete(long id);
}
