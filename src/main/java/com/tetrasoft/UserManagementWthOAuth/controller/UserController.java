package com.tetrasoft.UserManagementWthOAuth.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import com.tetrasoft.UserManagementWthOAuth.config.AuthorizationServerConfig;
import com.tetrasoft.UserManagementWthOAuth.dto.ApiResponse;
import com.tetrasoft.UserManagementWthOAuth.dto.UserDto;
import com.tetrasoft.UserManagementWthOAuth.service.UserService;

import io.restassured.RestAssured;
import io.restassured.response.Response;


@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	AuthorizationServerConfig auth;
	
	
    public static final String SUCCESS = "success";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_SUPERADMIN = "ROLE_SUPERADMIN";
    
    @Autowired
    private UserService userService;
    private String tokenValue = null;
    
    public void accessToken() {
    	  final Map<String, String> params = new HashMap<String, String>();
	        params.put("grant_type", "password");
	        params.put("client_id", "devglan-client");
	        params.put("username", "admin");
	        params.put("password", "admin");
	        final Response response = RestAssured.given().auth().preemptive().basic("devglan-client", "devglan-secret").and().with().params(params).when().post("http://localhost:9099/users/user/login");

	        tokenValue = response.jsonPath().getString("access_token");
    }
    
 
    @GetMapping(value="/getToken")
    public ApiResponse getToken() {
    	accessToken();
		return new ApiResponse(HttpStatus.OK, SUCCESS, tokenValue);
    	
    }
     
    @Secured({ROLE_SUPERADMIN,ROLE_ADMIN})
    @GetMapping
    public ApiResponse listUser(){   	
    	   return new ApiResponse(HttpStatus.OK, SUCCESS, userService.findAll());
    }    
    
    @Secured({ROLE_SUPERADMIN,ROLE_ADMIN})
    @PostMapping
    public ApiResponse create(@RequestBody UserDto user){
           return new ApiResponse(HttpStatus.OK, SUCCESS, userService.save(user));
    }

    @Secured({ROLE_SUPERADMIN,ROLE_ADMIN,ROLE_USER})
    @GetMapping(value = "/{id}")
    public ApiResponse getUser(@PathVariable long id){
             return new ApiResponse(HttpStatus.OK, SUCCESS, userService.findOne(id));
    }

    @Secured({ROLE_SUPERADMIN,ROLE_ADMIN,ROLE_USER})
    @PutMapping(value = "/{id}")
    public  ApiResponse updateUser(@PathVariable long id, @RequestBody UserDto user) {   	
    	 userService.delete(id);
    	 user.setId(id);
    	 return new ApiResponse(HttpStatus.OK, "User Updated Successfully", userService.save(user));	
    	
    }

    @Secured({ROLE_SUPERADMIN,ROLE_ADMIN})
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") Long id){
            userService.delete(id);
    }



}