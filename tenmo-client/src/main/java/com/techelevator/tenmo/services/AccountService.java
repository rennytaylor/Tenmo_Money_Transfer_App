package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {
    private String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken;
    AuthenticatedUser currentUser;

    public AccountService(String url, AuthenticatedUser currentUser) {
        this.baseUrl = url;
        this.currentUser = currentUser;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public BigDecimal getBalance(int userId) {
        BigDecimal balance = new BigDecimal("0");
        try {
            ResponseEntity<BigDecimal> response =
                    restTemplate.exchange(baseUrl + "account/balance/" + userId, HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }  return balance;
    }

    public Account getAccountByUserID(int userId){
        Account account = null;
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(baseUrl + "account/" + userId, HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }  return account;
    }

    public Account getAccountByAccountId(int accountId){
        Account account = null;
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(baseUrl + "account/accountId/" + accountId, HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }  return account;
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


    //Added methods below for sendBucks -------------------------------------------------------------------------------

//    public Account getAccountByUserId(int Id){
//
//    }
    public void increaseBalance(Account account, BigDecimal amount){
        Account recipientAccount = account;
            recipientAccount.setBalance(recipientAccount.getBalance().add(amount));
    }

    public void decreaseBalance(Account account, BigDecimal amount){
        Account sender = account;
            sender.setBalance(sender.getBalance().subtract(amount));
    }

    public void updateBalance(Account account){
        try {
            restTemplate.put(baseUrl + "account/updateBalance/" + account.getUserID(), makeAccountEntity(account));
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    public void updateBalances(Transfer transfer){
        Account accountFrom = getAccountByAccountId(transfer.getAccountFrom());
        Account accountTo = getAccountByAccountId(transfer.getAccountTo());
        decreaseBalance(accountFrom, transfer.getAmount());
        increaseBalance(accountTo, transfer.getAmount());
        try {
            updateBalance(accountFrom);
            updateBalance(accountTo);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }
}
