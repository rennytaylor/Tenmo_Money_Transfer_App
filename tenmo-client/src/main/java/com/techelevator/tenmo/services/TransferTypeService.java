package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferTypeService {
    private String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken;
    AuthenticatedUser currentUser;

    public TransferTypeService(String url, AuthenticatedUser currentUser) {
        this.baseUrl = url;
        this.currentUser = currentUser;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    public String getTransferTypeDesc(int transferTypeID) {
        TransferType transferTypeDesc = null;

        try {
            ResponseEntity<TransferType> response =
                    restTemplate.exchange(baseUrl + "transfertype/" + transferTypeID, HttpMethod.GET, makeAuthEntity(), TransferType.class);
            transferTypeDesc = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }  return transferTypeDesc.getTypeDesc();
    }



    private HttpEntity<TransferType> makeTransferTypeEntity(TransferType transferType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transferType, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
