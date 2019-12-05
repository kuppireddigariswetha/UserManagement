package com.tetrasoft.UserManagementWthOAuth.dao;

import org.springframework.data.repository.CrudRepository;
import com.tetrasoft.UserManagementWthOAuth.model.User;


public interface UserDao extends CrudRepository<User, Long> {

    User findByUsername(String username);
    User findByEmail(String email);
   
}
