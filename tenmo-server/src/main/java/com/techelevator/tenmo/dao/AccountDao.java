package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal viewCurrentBalance( int userId );

    BigDecimal getBalance( int userFrom );

    //Used in JdbcTransferDao
    BigDecimal subtractFromBalance( BigDecimal amount, int userFrom );

    //Used in JdbcTransferDao
    BigDecimal addToBalance( BigDecimal amount, int userTo );

    Account findUserById( int userId );

    Account findAccountById( int id);

//    Account create( Account account, int id, int userId );
//
//    Account update( Account account, int id );
//
//    boolean delete( int id, int userId );

}
