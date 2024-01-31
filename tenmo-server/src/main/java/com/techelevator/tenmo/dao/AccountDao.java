package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface AccountDao {
    public BigDecimal getAccountBalanceByUserID(int userID);
    Account getAccountByUserID(int userID);

    Account getAccountByAccountId(int accountId);

    Account updateBalance(Account account);

    @Transactional
    void updateAccountBalances(Transfer transfer);
}
