package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransferService {
    public static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Transfer sendMoney(Transfer newTransfer) {
    HttpEntity<Transfer> entity = makeTransferEntity(newTransfer);
    Transfer returnedTransfer = null;
    try {
        returnedTransfer = restTemplate.postForObject(API_BASE_URL + "transfers/transaction", entity, Transfer.class);
    }catch (ResourceAccessException e) {
        System.err.println(e.getMessage());
        BasicLogger.log(e.getMessage());
    } catch (RestClientResponseException e){
        System.err.println(e.getRawStatusCode());
        BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
    }
    System.out.println("\nYour transaction was successful.");
    return returnedTransfer;
    }

    public Transfer[] getAllTransfers(){
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfers",
                                        HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (ResourceAccessException e) {
            System.err.println(e.getMessage());
            BasicLogger.log(e.getMessage());
        } catch (RestClientResponseException e){
            System.err.println(e.getRawStatusCode());
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        }
        return transfers;
    }
    public Transfer getTransferById(int transferId){
        Transfer transfer = null;
        try{
            ResponseEntity< Transfer > response = restTemplate.exchange(API_BASE_URL + "transfers/" + transferId ,
                                        HttpMethod.GET, makeAuthEntity(), Transfer.class );
            transfer = response.getBody();
        } catch (ResourceAccessException e) {
            System.err.println(e.getMessage());
            BasicLogger.log(e.getMessage());
        } catch (RestClientResponseException e){
            System.err.println(e.getRawStatusCode());
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        }
        return transfer;
    }

    //Renny's additions for requestMoney
    public Transfer createRequestBucksTransferObject(int fromAccountID, int toAccountID, BigDecimal amount) {
        Transfer transferRequest = new Transfer();
        transferRequest.setTransferTypeId(1);
        transferRequest.setTransferStatusId(1);
        transferRequest.setAccountFrom(fromAccountID);
        transferRequest.setAccountTo(toAccountID);
        transferRequest.setAmount(amount);
        transferRequest.setTransferTypeDesc("Request");
        transferRequest.setTransferStatusDesc("Pending");

        return transferRequest;

    }

    public void requestBucks(Transfer requestTransfer){

        try {
        restTemplate.exchange(API_BASE_URL + "transfers/", HttpMethod.POST, makeTransferEntity(requestTransfer), Transfer.class);
        }catch (ResourceAccessException e) {
            System.err.println(e.getMessage());
            BasicLogger.log(e.getMessage());
        } catch (RestClientResponseException e){
            System.err.println(e.getRawStatusCode());
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        }
        System.out.println("\nYour request has been sent.");
    }
    //Renny's additions for viewPendingRequests
    public Transfer[] getTransferByAccountId(int fromAccountId){
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfers/mytransfers/" + fromAccountId,
                    HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (ResourceAccessException e) {
            System.err.println(e.getMessage());
            BasicLogger.log(e.getMessage());
        } catch (RestClientResponseException e){
            System.err.println(e.getRawStatusCode());
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        }
        return transfers;
    }

    public Transfer[] transferRequests(Transfer[] transfers, int fromAccountId){
        List<Transfer> transferRequests = new ArrayList<>();
        int pendingStatusId = 1;
        int requestTypeId = 1;
        for(Transfer transfer: transfers) {
            if (transfer.getAccountFrom() != fromAccountId
                    || transfer.getTransferStatusId() != pendingStatusId
                    || transfer.getTransferTypeId() != requestTypeId) {
                continue;
            } transferRequests.add(transfer);
        }
        Transfer[] transferArray = transferRequests.toArray(new Transfer[transferRequests.size()]);
        return transferArray;
    }

    public Transfer updateTransfer(Transfer transfer, int statusId){
        transfer.setTransferStatusId(statusId);
        Transfer newTransfer = new Transfer();
        try {
            newTransfer = restTemplate.exchange(API_BASE_URL + "transfers/status/" + statusId, HttpMethod.PUT, makeTransferEntity(transfer), Transfer.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return newTransfer;
    }



    //End Renny's additions

    //added for sendBucks
    public Transfer createSendBucksTransferObject(int fromAccountID, int toAccountID, BigDecimal amount) {
        Transfer transfer = new Transfer();
        transfer.setAccountFrom(fromAccountID);
        transfer.setAccountTo(toAccountID);
        transfer.setAmount(amount);
        transfer.setTransferTypeId(2);
        transfer.setTransferStatusId(2);
        transfer.setTransferStatusDesc("Approved");
        transfer.setTransferTypeDesc("Send");


        return transfer;
    }
    public void createSendBucksTransfer(Transfer transfer){
        restTemplate.exchange(API_BASE_URL + "transfers/", HttpMethod.POST, makeTransferEntity(transfer), Transfer.class);
    }
    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
