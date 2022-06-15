package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private int id;
    private int userId;
    private BigDecimal balance;

    public Account(){}

    public Account( int id, int userId, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId( int userId ) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance( BigDecimal balance ) {
        this.balance = balance;
    }
}
