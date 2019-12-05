package com.tetrasoft.UserManagementWthOAuth.service.serviceImpl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.tetrasoft.UserManagementWthOAuth.service.AuthenticationFacadeService;

@Component
public class AuthenticationFacadeServiceImpl implements AuthenticationFacadeService {

	  @Override
	    public Authentication getAuthentication() {
	        return SecurityContextHolder.getContext().getAuthentication();
	    }
}
