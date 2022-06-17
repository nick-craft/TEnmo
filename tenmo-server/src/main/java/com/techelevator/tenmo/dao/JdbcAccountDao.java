package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    @Autowired
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
    public BigDecimal getBalance( int userId ) {
        String sqlString = "SELECT balance FROM accounts WHERE user_id = ?";
        SqlRowSet results = null;
        BigDecimal balance = null;
        try {
            results = jdbcTemplate.queryForRowSet(sqlString, userId);
            if (results.next()) {
                balance = results.getBigDecimal("balance");
            }
        } catch (DataAccessException e) {
            System.out.println("Error accessing data");
        }
        return balance;
    }

    @Override
    public BigDecimal subtractFromBalance( BigDecimal amount, int userFrom ) {
        Account account = findAccountById(userFrom);
        BigDecimal newBalance = account.getBalance().subtract(amount);
        String sqlString = "UPDATE accounts SET balance = ? WHERE user_id = ?";
        try {
            jdbcTemplate.update(sqlString, newBalance, userFrom);
        } catch (DataAccessException e) {
            System.out.println("Error accessing data");
        }
        return account.getBalance();
    }


    @Override
    public BigDecimal addToBalance( BigDecimal amount, int userTo ) {
        Account account = findAccountById(userTo);
        BigDecimal newBalance = account.getBalance().add(amount);
        System.out.println(newBalance);
        String sqlString = "UPDATE accounts SET balance = ? WHERE user_id = ?";
        try {
            jdbcTemplate.update(sqlString, newBalance, userTo);
        } catch (DataAccessException e) {
            System.out.println("Error accessing data");
        }
        return account.getBalance();
    }

    @Override
    public Account findUserById(int userId) {
        String sqlString = "SELECT * FROM accounts WHERE user_id = ?";
        Account account = null;
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sqlString, userId);
            account = mapRowToAccount(result);
        } catch (DataAccessException e) {
            System.out.println("Error accessing data");
        }
        return account;
    }


//    @Override
//    public Account create( Account account, int id, int userId ){
//        String sql = "INSERT into account (account_id, user_id, balance)" +
//                "VALUES (?,?,1000)";
//        jdbcTemplate.update(sql, account, id, userId);
//        return account;
//    }
//
//    @Override
//    public Account update( Account account, int id ) {
//        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
//        jdbcTemplate.update(sql, account.getBalance(), id);
//        return account;
//    }
//
//    @Override
//    public boolean delete( int id, int userId ) {
//        String sql = "DELETE FROM account WHERE account_id = ? AND user_id = ?";
//        if (id != 0){
//            jdbcTemplate.update(sql, id, userId);
//        }
//        return false;
//    }

    @Override
    public Account findAccountById(int id) {
        Account account = null;
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    private Account mapRowToAccount(SqlRowSet result) {
        Account account = new Account();

        account.setId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        account.setBalance(result.getBigDecimal("balance"));
        return account;
    }
}
