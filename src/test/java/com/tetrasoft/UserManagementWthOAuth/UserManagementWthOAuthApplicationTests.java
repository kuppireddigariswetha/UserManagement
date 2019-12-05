package com.tetrasoft.UserManagementWthOAuth;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.tetrasoft.UserManagementWthOAuth.controller.UserController;
import com.tetrasoft.UserManagementWthOAuth.dto.UserDto;
import com.tetrasoft.UserManagementWthOAuth.service.UserService;

import io.restassured.RestAssured;
import io.restassured.response.Response;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = {
		UserManagementWthOAuthApplication.class, 
		  Oauth2JpaConfig.class})
@ActiveProfiles("test")
class UserManagementWthOAuthApplicationTests {

	@Autowired
    private UserService userService;
	
	@InjectMocks
	private UserController userController;
	
    @Autowired
    private WebApplicationContext wac;
 
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
 
    private MockMvc mockMvc;
 
    @Before
    public void setup() {
    	 MockitoAnnotations.initMocks(this);
         this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
    
    private static final String URL_PREFIX = "http://localhost:9099";
    private String tokenValue = null;

    @Before
    public void obtainAccessToken() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("client_id", "devglan-client");
        params.put("username", "admin");
        params.put("password", "admin");
        final Response response = RestAssured.given().auth().preemptive().basic("devglan-client", "devglan-secret").and().with().params(params).when().post("http://localhost:9099/users/user/login");

        tokenValue = response.jsonPath().getString("access_token");
    }
   
    
    @Test
    public void whenVerifyGetEmployeesWorking() {
        Response response = RestAssured.get(URL_PREFIX + "/users");
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
       
        response = RestAssured.given().header("Authorization", "Bearer " + tokenValue).get(URL_PREFIX + "/users");
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        System.out.println("inside =80"+ response.asString());
        
    }

    @Test
    public void givenToken_whenPostGetSecureRequest_thenOk() throws Exception {
    	
    	 UserDto userDto=new UserDto();
    	 List<String> role=new ArrayList<String>();
    	 role.add("USER");
	     userDto.setId(2);
	     userDto.setFirstName("swetha");
	     userDto.setLastName("kuppi");
	     userDto.setUsername("swetha");
	     userDto.setPassword("swetha");
	     userDto.setEmail("swetha@gmail.com");
	     userDto.setRole(role);	     
	     
	     UserDto u= userService.save(userDto);
		 assertEquals("swetha",u.getFirstName());
		 assertEquals("swetha@gmail.com",u.getEmail());

    }	
    	   
}
