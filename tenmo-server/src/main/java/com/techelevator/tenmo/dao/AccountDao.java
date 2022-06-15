package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal viewCurrentBalance(int userId);

    void addToBalance( Account account );


//    Account get( int userId ) throws AccountNotFoundException;


    Account create( Account account, int id, int userId );

    Account update( Account account, int id );

    boolean delete( int id, int userId );

    Account get( int userId, BigDecimal balance );
}
