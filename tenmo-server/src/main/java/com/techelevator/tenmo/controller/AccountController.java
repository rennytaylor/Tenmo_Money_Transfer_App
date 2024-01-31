package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;


@RestController
public class AccountController {


    private AccountDao accountDao;

    public AccountController(AccountDao account) {
        this.accountDao = account;
    }

    @RequestMapping(path = "account/balance/{userId}", method = RequestMethod.GET)
    public BigDecimal getBalanceByUserId(@PathVariable int userId) {
        return accountDao.getAccountBalanceByUserID(userId);
    }

    @RequestMapping(path = "account/{userId}", method = RequestMethod.GET)
    public Account getAccountByUserID(@PathVariable int userId){
        return accountDao.getAccountByUserID(userId);
    }

    @RequestMapping(path = "account/accountId/{accountId}", method = RequestMethod.GET)
    public Account getAccountByAccountId(@PathVariable int accountId){
        return accountDao.getAccountByAccountId(accountId);
    }

    @RequestMapping(path = "account/updateBalance/{userId}", method = RequestMethod.PUT)
    public Account updateBalance(@RequestBody Account account, @PathVariable int userId){
        userId = account.getUserID();
        return accountDao.updateBalance(account);


    }

    @RequestMapping(path = "account/updateBalances", method = RequestMethod.PUT)
    public void updateBalances(@RequestBody Transfer transfer){
        accountDao.updateAccountBalances(transfer);


    }


}
