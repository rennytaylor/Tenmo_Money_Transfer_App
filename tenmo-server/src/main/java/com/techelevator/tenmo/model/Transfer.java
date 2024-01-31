package com.techelevator.tenmo.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    @Min( value = 1, message = "Enter valid transferTypeId")
    private int transferTypeId;
    @Min( value = 1, message = "Enter valid transferStatusId")
    private int transferStatusId;
    @Min( value = 1, message = "Enter valid accountFrom")
    private int accountFrom;
    @Min( value = 1, message = "Enter valid accountTo")
    private int accountTo;
    @Min( value = 1, message = "The minimum amount of TE bucks to transfer is 1")
    private BigDecimal amount;

    //debating on creating a transferType object and transferStatus object to return description for each
    private String transferTypeDesc;
    private String transferStatusDesc;

    //constructors
    public Transfer(){}

    public Transfer(int transferId, int transferTypeId, int transferStatusId, int accountFrom, int accountTo,
                    BigDecimal amount, String transferTypeDesc, String transferStatusDesc) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.transferTypeDesc = transferTypeDesc;
        this.transferStatusDesc = transferStatusDesc;
    }



    //getters and setters
    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }

    public void setTransferTypeDesc(String transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }

    //generated toString
    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", transferType='" + transferTypeId + '\'' +
                ", transferStatus='" + transferStatusId + '\'' +
                ", accountFrom='" + accountFrom + '\'' +
                ", accountTo='" + accountTo + '\'' +
                ", amount=" + amount +
                ", transferTypeDesc='" + transferTypeDesc + '\'' +
                ", transferStatusDesc='" + transferStatusDesc + '\'' +
                '}';
    }
}
