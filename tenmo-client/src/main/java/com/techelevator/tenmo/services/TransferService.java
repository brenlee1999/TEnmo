package com.techelevator.tenmo.services;

import com.techelevator.tenmo.App;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class TransferService {
    public static final String API_BASE_URL = "http://localhost:8080/";

    RestTemplate restTemplate = new RestTemplate();
    AccountService accountService = new AccountService();
    UserService userService = new UserService();
    User user = new AuthenticatedUser().getUser();
    private String authToken = null;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    public void createTransfer(int transferId, int transferTypeId, int transferStatusId, int accountFromId, int accountToId, BigDecimal transferAmount) {
        Transfer newTransfer = new Transfer(transferId, transferTypeId, transferStatusId, accountFromId, accountToId, transferAmount);
        restTemplate.exchange(API_BASE_URL + "transfer", HttpMethod.POST, makeTransferEntity(newTransfer), Transfer.class);
        //Transfer transfer = response.getBody();
    }

    public void transferMoney(int senderId, int recipientId) {
        accountService.setAuthToken(getAuthToken());
        BigDecimal moneyInput;
        Account updatedSenderAccount = new Account();
        Account updatedReceiverAccount = new Account();
        Scanner input = new Scanner(System.in);

        BigDecimal senderAccountBalance = accountService.getAccountBalance(senderId);
        BigDecimal receiverAccountBalance = accountService.getAccountBalance(recipientId);

        System.out.println("Please enter the amount of money you'd like to send: ");
        moneyInput = input.nextBigDecimal();

        if (moneyInput.compareTo(BigDecimal.ZERO) <= 0 || senderAccountBalance.compareTo(moneyInput) < 0) {

            System.out.println("Please try again, non-negative numbers or 0/Insufficient funds for transfer, please try again.");
        }
        //System.out.println("Insufficient funds for transfer, please try again.");

        System.out.println("Receiving sender and receiver accounts...");
        if (senderId != recipientId) {
            updatedSenderAccount = new Account();
            updatedSenderAccount.setAccountId(senderId + 1000-3);
            updatedSenderAccount.setUserId(senderId);
            updatedSenderAccount.setBalance(senderAccountBalance.subtract(moneyInput));
            System.out.println("Sender Account received.");
            updatedReceiverAccount = new Account();
            updatedReceiverAccount.setAccountId(recipientId + 1000-3);
            updatedReceiverAccount.setUserId(recipientId);
            updatedReceiverAccount.setBalance(receiverAccountBalance.add(moneyInput));
            System.out.println("Receiver Account received.");
        }

        try {
            System.out.println("Attempting to send money...");
            accountService.updateBalance(updatedSenderAccount);
            accountService.updateBalance(updatedReceiverAccount);
            /*HttpEntity<Account> requestSenderUpdate = new HttpEntity<>(updatedSenderAccount, makeAuthEntity().getHeaders());
            HttpEntity<Account> requestReceiverUpdate = new HttpEntity<>(updatedReceiverAccount, makeAuthEntity().getHeaders());
            restTemplate.exchange(API_BASE_URL + "account/balance?user_id_like=" + senderId, HttpMethod.PUT, requestSenderUpdate, Void.class).getBody();
            restTemplate.exchange(API_BASE_URL + "account/balance?user_id_like=" + recipientId, HttpMethod.PUT, requestReceiverUpdate, Void.class).getBody();*/
        } catch (RestClientResponseException e) {
            System.out.println("Error");
        }
        //createTransfer(int transferId, int transferTypeId, int transferStatusId, int accountFromId, int accountToId, BigDecimal transferAmount)
        System.out.println("Logging transfer...");
        createTransfer(getMaxID()+1, 2 , 2, updatedSenderAccount.getAccountId(), updatedReceiverAccount.getAccountId(), moneyInput);
        System.out.println("Transfer logged!\nMoney transfer completed!");
    }

    public List<Transfer> getListOfTransfers(int userID) {
        Transfer[] transfers = new Transfer[]{};
        try {
            transfers = restTemplate.exchange(API_BASE_URL + "transfer?user_id_like=" + userID, HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("User is not authorized.");
        } catch (HttpClientErrorException.NotFound e){
            System.out.println("Address is not found.");
        } catch (NullPointerException e) {
            System.out.println("List of Transfers are null.");
        }
        return Arrays.asList(Objects.requireNonNull(transfers));
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    public int getMaxID(){
        Transfer[] transfers = null;
        try {
            transfers = restTemplate.exchange(API_BASE_URL + "transfer", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("User is not authorized.");
        }
        assert transfers != null;
        return transfers.length;
    }
}
