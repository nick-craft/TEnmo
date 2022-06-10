package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal viewCurrentBalance(int userId);

    void addToBalance( Account account );


    Account get( int userId ) throws AccountNotFoundException;
}
