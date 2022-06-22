package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transferId;
    private int transferTypeId;
    private int transferStatusId;
    private int accountFrom;
    private int accountTo;
    private BigDecimal amount;

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo( String userTo ) {
        this.userTo = userTo;
    }

    private String userFrom;
    private String userTo;

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom( String userFrom ) {
        this.userFrom = userFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo( int accountTo ) {
        this.accountTo = accountTo;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId( int transferStatusId ) {
        this.transferStatusId = transferStatusId;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId( int transferId ) {
        this.transferId = transferId;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId( int transferTypeId ) {
        this.transferTypeId = transferTypeId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom( int accountFrom ) {
        this.accountFrom = accountFrom;
    }

    public void setAmount( BigDecimal amount ) {
        this.amount = amount;
    }
}