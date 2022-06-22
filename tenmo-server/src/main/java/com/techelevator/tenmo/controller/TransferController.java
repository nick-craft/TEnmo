package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("permitAll")
public class TransferController {

    @Autowired
    private TransferDao transferDao;


    @RequestMapping(value = "account/transfers/{id}", method = RequestMethod.GET)
    public List<Transfer> viewTransferHistory( @PathVariable int id ) {
        return transferDao.viewTransferHistory(id);
    }

    @RequestMapping(path = "request/pending/{id}", method = RequestMethod.GET)
    public List<Transfer> viewPendingRequests(@PathVariable int id) {
        return transferDao.viewPendingRequests(id);
    }

    @RequestMapping(path = "transfer", method = RequestMethod.POST)
    public void sendBucks(@RequestBody Transfer transfer) {
        transferDao.sendBucks(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @RequestMapping(path = "transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id) {
        Transfer transfer = transferDao.getTransferById(id);
        return transfer;
    }

    @RequestMapping(path = "request", method = RequestMethod.POST)
    public void requestBucks(@RequestBody Transfer transfer) {
        transferDao.requestBucks(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @RequestMapping(path = "request/update/{transferStatusId}", method = RequestMethod.PUT)
    public void updateTransferRequest(@RequestBody Transfer transfer, @PathVariable int transferStatusId) {
        transferDao.updateTransferRequest(transfer, transferStatusId);
    }

}
