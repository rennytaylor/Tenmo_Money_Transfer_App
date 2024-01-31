package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    Transfer getTransferByTransferId(int transferId);
    List<Transfer> getAllTransfersByUserId(int userId);
    Transfer updateTransfer(Transfer transfer, int statusId);
    int deleteTransferById(int id);
    Transfer createTransfer(Transfer transfer);
    void updateAccountBalances(int accountFrom, int accountTo, BigDecimal amount);

    List<Transfer> getTransfersByAccountId(int accountId);

    //Renny's additions for Pending

}