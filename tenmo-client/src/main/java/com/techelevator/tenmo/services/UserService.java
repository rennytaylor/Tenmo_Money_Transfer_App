package com.techelevator.tenmo.services;

import com.techelevator.tenmo.App;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class UserService {
    private final String API_BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    public AuthenticatedUser currentUser;

    private String authToken;
    public UserService(String baseUrl, AuthenticatedUser currentUser){
        this.API_BASE_URL = baseUrl;
        this.currentUser = currentUser;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public User[] listUsers() {
        //return restTemplate.getForObject(API_BASE_URL, User[].class);
        User[] users = null;
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(API_BASE_URL + "users", HttpMethod.GET, makeAuthEntity(), User[].class);
            users = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return users;
    }

    public User[] listUsernames() {
        //return restTemplate.getForObject(API_BASE_URL, User[].class);
        User[] users = null;
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(API_BASE_URL + "username", HttpMethod.GET, makeAuthEntity(), User[].class);
            users = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return users;
    }

    public User getUserById(int id){
        User user = null;

        try {
            ResponseEntity<User> response =
                    restTemplate.exchange(API_BASE_URL + "users/" + id , HttpMethod.GET, makeAuthEntity(), User.class);
            user = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return user;
    }

    //additions for requestBucks, code is cute, might delete later, idk -Renny

    public boolean checkThatRequestIDIsNotCurrentUserID(int requestID, int currentUserID){
        boolean isItCurrentUser = false;
        if (requestID == currentUserID) {
            isItCurrentUser = true;
        } return isItCurrentUser;
    }

    public boolean doesUserExistByID(int userID){
        boolean hasUser = false;
        User user = null;
        try {
            ResponseEntity<User> response =
                    restTemplate.exchange(API_BASE_URL + "users/exist/" + userID , HttpMethod.GET, makeAuthEntity(), User.class);
            user = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        if(user != null){
            hasUser = true;
        }
        return hasUser;

    }

    //end of Renny's additions

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
