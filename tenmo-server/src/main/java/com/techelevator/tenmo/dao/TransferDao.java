package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> viewTransferHistory( int userId );

    List<Transfer> viewPendingRequests(int userId);

    String sendBucks( int accountFrom, int accountTo, BigDecimal amount);

    Transfer getTransferById(int transferId);

    String requestBucks( int accountFrom, int accountTo, BigDecimal amount );

    String updateTransferRequest( Transfer transfer, int transferStatusId );
}
