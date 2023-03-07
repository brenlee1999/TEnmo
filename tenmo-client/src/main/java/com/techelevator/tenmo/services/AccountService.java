package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AccountService {
    public static final String API_BASE_URL = "http://localhost:8080/account/";
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public BigDecimal getAccountBalance(int userId) {
        BigDecimal balance = null;
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL +
                    "balance?user_id_like=" + userId, HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("User is not authorized.");
        }
        if (balance != null)
            return balance;
        else
            System.out.println(" Account is null. ");
        return BigDecimal.valueOf(0);
    }

    public void updateBalance(Account account) {
        restTemplate.exchange(API_BASE_URL + "?account_id_like=" + account.getAccountId(), HttpMethod.PUT, makeAccountEntity(account), Void.class);
    }

    public List<Account> getAccounts() {
        Account[] accountsList = null;
        try {
            accountsList = restTemplate.exchange(API_BASE_URL + "account", HttpMethod.GET, makeAuthEntity(), Account[].class).getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("User is not authorized.");
        }
        return Arrays.asList(Objects.requireNonNull(accountsList));
    }

    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(account, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
