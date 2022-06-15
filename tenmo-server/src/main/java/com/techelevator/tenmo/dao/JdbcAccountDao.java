package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal viewCurrentBalance(int userId) {
        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        SqlRowSet results = null;
        BigDecimal balance = null;

        try {
            results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                balance = results.getBigDecimal("balance");
            }
        } catch (DataAccessException e) {
            System.out.println("Sorry, not found");
        }
        return balance;
    }

    @Override
    public void addToBalance( Account account ) {
        String sql = "UPDATE account SET balance = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, account.getBalance(), account.getUserId());
    }

    @Override
    public Account get( int userId, BigDecimal balance ) {
        Account account = null;
        String sql = "SELECT account FROM tenmo WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if(results.next()){
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public Account create( Account account, int id, int userId ){
        String sql = "INSERT into account (account_id, user_id, balance)" +
                "VALUES (?,?,1000)";
        jdbcTemplate.update(sql, account, id, userId);
        return account;
    }

    @Override
    public Account update( Account account, int id ) {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, account.getBalance(), id);
        return account;
    }

    @Override
    public boolean delete( int id, int userId ) {
        String sql = "DELETE FROM account WHERE account_id = ? AND user_id = ?";
        if (id != 0){
            jdbcTemplate.update(sql, id, userId);
        }
        return false;
    }

    private Account mapRowToAccount(SqlRowSet result) {
        Account account = new Account();

        account.setId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        account.setBalance(result.getBigDecimal("balance"));
        return account;
    }



    //    if (toUserId != fromUserId){
//        send
//    } else {
//        throw exception "cannot send self money"
//    }
//    return send

}
