package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public class JdbcTransferDao implements TransferDao{

    @Override
    public List<Transfer> viewTransferHistory() {
        return null;
    }

    @Override
    public List<Transfer> viewPendingRequests(){
        return null;
    }

    @Override
    public String sendBucks() {
        return null;
    }
}
