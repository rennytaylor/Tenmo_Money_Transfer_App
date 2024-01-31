package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferStatusService {

    private String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken;
    AuthenticatedUser currentUser;

    public TransferStatusService(String url, AuthenticatedUser currentUser) {
        this.baseUrl = url;
        this.currentUser = currentUser;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    public String getTransferStatusDesc(int transferStatusID) {
        TransferStatus transferStatusDesc = null;

        try {
            ResponseEntity<TransferStatus> response =
                    restTemplate.exchange(baseUrl + "transferstatus/desc/" + transferStatusID, HttpMethod.GET, makeAuthEntity(), TransferStatus.class);
            transferStatusDesc = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }  return transferStatusDesc.getStatusDesc();
    }



    private HttpEntity<TransferStatus> makeTransferStatusEntity(TransferStatus transferStatus) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transferStatus, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

}
