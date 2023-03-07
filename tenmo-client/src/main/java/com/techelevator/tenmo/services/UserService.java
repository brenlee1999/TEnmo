package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserService {
    public static final String API_BASE_URL = "http://localhost:8080/tenmo/";
    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    RestTemplate restTemplate = new RestTemplate();

    public User getUserById(int userId){
        User user = null;
        return user = restTemplate.exchange(API_BASE_URL + "tenmo-user?user_id_like=" + userId, HttpMethod.GET, makeAuthEntity(), User.class).getBody();
    }
    public List<User> getListofUsers() {
        User[] userList = null;
        try {
            userList = restTemplate.exchange(API_BASE_URL + "tenmo-user", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("User is not authorized.");
        }
        return Arrays.asList(Objects.requireNonNull(userList));
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
