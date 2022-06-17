package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDao implements TransferDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> viewTransferHistory( int userId ) {
        List<Transfer> list = new ArrayList<>();
        String sql = "SELECT t.*, u1.username AS userFrom, u2.username AS userTo FROM transfers t " +
                "JOIN accounts a1 ON t.account_from = a1.account_id " +
                "JOIN accounts a2 ON t.account_to = a2.account_id " +
                "JOIN users u1 ON a1.user_id = u1.user_id " +
                "JOIN users u2 ON a2.user_id = u2.user_id " +
                "WHERE a.user_id = ? OR b.user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next() ) {
            Transfer transfer = mapRowToTransfer(results);
            list.add(transfer);
        }
        return list;
    }

    @Override
    public List<Transfer> viewPendingRequests( int userId ) {
        List<Transfer> output = new ArrayList<>();
        String sql = "SELECT t.*, u1.username AS userFrom, u2.username AS userTo FROM transfers t " +
                "JOIN accounts a1 ON t.account_from = a1.account_id " +
                "JOIN accounts a2 ON t.account_to = a2.account_id " +
                "JOIN users u1 ON a1.user_id = u1.user_id " +
                "JOIN users u2 ON a2.user_id = u2.user_id " +
                "WHERE transfer_status_id = 1 AND (account_from = ? OR account_to = ?)";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            output.add(transfer);
        }
        return output;
    }

    @Override
    public String sendBucks(int userFrom, int userTo, BigDecimal amount) {
        if (userFrom == userTo) {
            return "User can't send money to self";
        }
        if (amount.compareTo(accountDao.getBalance(userFrom)) == -1 && amount.compareTo(new BigDecimal(0)) == 1) {
            String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (2, 2, ?, ?, ?)";
            jdbcTemplate.update(sql, userFrom, userTo, amount);
            accountDao.addToBalance(amount, userTo);
            accountDao.subtractFromBalance(amount, userFrom);
            return "Transfer complete";
        } else {
            return "Transfer failed, you're probably broke like me";
        }
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setId(results.getInt("transfer_id"));
        transfer.setTypeId(results.getInt("transfer_type_id"));
        transfer.setStatusId(results.getInt("transfer_status_id"));
        transfer.setFromAccount(results.getInt("account_From"));
        transfer.setToAccount(results.getInt("account_to"));
        transfer.setTransferAmount(results.getBigDecimal("amount"));
        try {
            transfer.setFromUser(results.getString("userFrom"));
            transfer.setToUser(results.getString("userTo"));
        } catch (Exception e) {}
        try {
            transfer.setTypeTransfer(results.getString("transfer_type_desc"));
            transfer.setStatusTransfer(results.getString("transfer_status_desc"));
        } catch (Exception e) {}
        return transfer;
    }
}
