package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    private int userID;
    private int accountID;
    private BigDecimal balance;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    //needed for testing?
//
//    public  Account(){}
//
//    public Account(int userID, int accountID, BigDecimal balance){}

}
