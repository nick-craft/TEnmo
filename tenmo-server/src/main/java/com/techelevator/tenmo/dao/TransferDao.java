package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> viewTransferHistory();

    List<Transfer> viewPendingRequests();

    String sendBucks();
}
