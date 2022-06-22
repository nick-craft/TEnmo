package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

//    BigDecimal viewCurrentBalance( int userId );

    BigDecimal getBalance( int userId );

    //Used in JdbcTransferDao
    BigDecimal subtractFromBalance( BigDecimal amount, int fromAccount );

    //Used in JdbcTransferDao
    BigDecimal addToBalance( BigDecimal amount, int toAccount );

    Account findUserById( int userId );

    Account findAccountById( int id);


}
