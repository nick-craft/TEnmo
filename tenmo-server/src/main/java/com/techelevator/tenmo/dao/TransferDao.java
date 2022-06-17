package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> viewTransferHistory( int userId );

    List<Transfer> viewPendingRequests(int userId);

    String sendBucks(int userFrom, int userTo, BigDecimal amount);
}
