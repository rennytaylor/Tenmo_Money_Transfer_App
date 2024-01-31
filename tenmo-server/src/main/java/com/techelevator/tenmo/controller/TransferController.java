package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "transfers/")
public class TransferController {

    TransferDao transferDao;

    UserDao userDao;

    AccountDao accountDao;

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Transfer> getListOfTransfers(Principal principal) {
        User user = userDao.getUserByUsername(principal.getName());
        return transferDao.getAllTransfersByUserId(user.getId());
    }

    //Added "transaction" here because it wouldn't let two post methods post to the same url path. You can change it, just did
    //it to finish up requestBucks. createTransfer is currently the one posting to the /transfers url path.
    @RequestMapping(path = "transaction", method = RequestMethod.POST)
    public Transfer transaction (@RequestBody Transfer transfer, Principal principal){
        Transfer newTransfer = null;
        User user = userDao.getUserByUsername(principal.getName());
        Account account = accountDao.getAccountByUserID(user.getId());

        if(transfer.getAccountFrom() == transfer.getAccountTo()) {
            throw new DaoException("Cannot Transfer to self");
        } else if(transfer.getAccountFrom() == account.getAccountID()){
            newTransfer = transferDao.createTransfer(transfer);
            transferDao.updateAccountBalances(account.getAccountID(), transfer.getAccountTo(), transfer.getAmount());
        } else {
            throw new DaoException("Can only send money from your own account");
        }
        return newTransfer;
    }

    @RequestMapping(path="{transferId}", method = RequestMethod.GET)
    public Transfer getTransferByTransferId(@PathVariable int transferId) throws Exception {
        return transferDao.getTransferByTransferId(transferId);
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public Transfer createTransfer(@RequestBody Transfer transfer) {
        return transferDao.createTransfer(transfer);
    }

    @RequestMapping(path = "mytransfers/{accountId}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByAccountId(@PathVariable int accountId) {
        return transferDao.getTransfersByAccountId(accountId);
    }

    @RequestMapping(path = "status/{statusId}", method = RequestMethod.PUT)
    public Transfer approveTransfer(@RequestBody Transfer transfer, @PathVariable int statusId){
        return transferDao.updateTransfer(transfer, statusId);
    }


}
