package com.techelevator.tenmo.model;

public class TransferType {
    private int typeID;
    private String typeDesc;
    public int getTypeID() {
        return typeID;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

}
