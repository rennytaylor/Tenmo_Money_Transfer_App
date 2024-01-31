package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferTypeDao;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferTypeController {
    private TransferTypeDao transferTypeDao;

    public TransferTypeController(TransferTypeDao transferTypeDao) {
        this.transferTypeDao = transferTypeDao;
    }

    @RequestMapping(path = "transfertype/{transferTypeID}", method = RequestMethod.GET)
    public TransferType getTransferTypeByTypeID(@PathVariable int transferTypeID) {
        return transferTypeDao.getTransferTypeByTypeID(transferTypeID);
    }
}
