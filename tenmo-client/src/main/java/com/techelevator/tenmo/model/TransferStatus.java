package com.techelevator.tenmo.model;

public class TransferStatus {

    private int statusID;

    private String statusDesc;
    public int getStatusID() {
        return statusID;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

}
