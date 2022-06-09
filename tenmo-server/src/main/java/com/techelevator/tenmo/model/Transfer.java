package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private BigDecimal transferAmount;
    private String typeTransfer;
    private String statusTransfer;
    private String fromUser;
    private String toUser;
    private int id;
    private int typeId;
    private int statusId;
    private int fromAccount;
    private int toAccount;

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount( BigDecimal transferAmount ) {
        this.transferAmount = transferAmount;
    }

    public String getTypeTransfer() {
        return typeTransfer;
    }

    public void setTypeTransfer( String typeTransfer ) {
        this.typeTransfer = typeTransfer;
    }

    public String getStatusTransfer() {
        return statusTransfer;
    }

    public void setStatusTransfer( String statusTransfer ) {
        this.statusTransfer = statusTransfer;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser( String fromUser ) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser( String toUser ) {
        this.toUser = toUser;
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId( int typeId ) {
        this.typeId = typeId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId( int statusId ) {
        this.statusId = statusId;
    }

    public int getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount( int fromAccount ) {
        this.fromAccount = fromAccount;
    }

    public int getToAccount() {
        return toAccount;
    }

    public void setToAccount( int toAccount ) {
        this.toAccount = toAccount;
    }
}
