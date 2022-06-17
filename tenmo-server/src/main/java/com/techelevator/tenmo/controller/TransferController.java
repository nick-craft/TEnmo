package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("isValidUser()")
public class TransferController {

    private TransferDao transferDao;

    @RequestMapping(value = "account/transfers/{id}", method = RequestMethod.GET)
    public List<Transfer> viewTransferHistory( @PathVariable int id ) {
        List<Transfer>  output = transferDao.viewTransferHistory(id);
        return output;
    }

    @RequestMapping(path = "request/{id}", method = RequestMethod.GET)
    public List<Transfer> viewPendingRequests(@PathVariable int id) {
        List<Transfer> output = transferDao.viewPendingRequests(id);
        return output;
    }

    @RequestMapping(path = "transfer", method = RequestMethod.POST)
    public String sendBucks(@RequestBody Transfer transfer) {
        String results = transferDao.sendBucks(transfer.getFromAccount(), transfer.getToAccount(), transfer.getTransferAmount());
        return results;
    }

}
