package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.json.GsonBuilderUtils;
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
    public BigDecimal getBalance( int userId ) {
        String sqlString = "SELECT balance FROM account WHERE user_id = ?";
        SqlRowSet results;
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
    public BigDecimal subtractFromBalance( BigDecimal amount, int fromAccount) {
        Account account = findAccountById(fromAccount);
        BigDecimal newBalance = account.getBalance().subtract(amount);
        String sqlString = "UPDATE account SET balance = ? WHERE user_id = ?";
        try {
            jdbcTemplate.update(sqlString, newBalance, fromAccount);
        } catch (DataAccessException e) {
            System.out.println("Error accessing data");
        }
        return account.getBalance();
    }


    @Override
    public BigDecimal addToBalance( BigDecimal amount, int toAccount ) {
        Account account = findAccountById(toAccount);
        BigDecimal newBalance = account.getBalance().add(amount);
        System.out.println(newBalance);
        String sqlString = "UPDATE account SET balance = ? WHERE user_id = ?";
        try {
            jdbcTemplate.update(sqlString, newBalance, toAccount);
        } catch (DataAccessException e) {
            System.out.println("Error accessing data");
        }
        return account.getBalance();
    }

    @Override
    public Account findUserById(int userId) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE user_id = ?";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
            account = mapRowToAccount(result);
        } catch (DataAccessException e) {
            System.out.println("Error accessing data");
        }
        return account;
    }


    @Override
    public Account findAccountById(int id) {
        Account account = new Account();
        String sql = "SELECT * FROM account WHERE user_id = ?";
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
