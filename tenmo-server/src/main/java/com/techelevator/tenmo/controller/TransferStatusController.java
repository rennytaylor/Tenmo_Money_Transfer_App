package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferStatusDao;
import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferStatusController {

    private TransferStatusDao transferStatusDao;

    public TransferStatusController(TransferStatusDao transferStatusDao) {
        this.transferStatusDao = transferStatusDao;
    }

    @RequestMapping(path = "transferstatus/desc/{transferStatusID}", method = RequestMethod.GET)
    public TransferStatus getTransferStatusDescByStatusID(@PathVariable int transferStatusID) {
        return transferStatusDao.getTransferStatusDescByStatusID(transferStatusID);
    }
}
