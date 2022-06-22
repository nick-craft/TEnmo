package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
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
        String sql = "SELECT t.*, u1.username AS userFrom, u2.username AS userTo FROM transfer t " +
                "JOIN account a1 ON t.account_from = a1.account_id " +
                "JOIN account a2 ON t.account_to = a2.account_id " +
                "JOIN tenmo_user u1 ON a1.user_id = u1.user_id " +
                "JOIN tenmo_user u2 ON a2.user_id = u2.user_id " +
                "WHERE u1.user_id = ? OR u1.user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next() ) {
            Transfer transfer = mapRowToTransfer(results);
            list.add(transfer);
        }
        return list;
    }

    @Override
    public List<Transfer> viewPendingRequests( int userId ) {
        List<Transfer> pending = new ArrayList<>();
        String sql = "SELECT t.*, u1.username AS userFrom, u2.username AS userTo FROM transfer t\n" +
                "                JOIN account a1 ON t.account_from = a1.account_id\n" +
                "                JOIN account a2 ON t.account_to = a2.account_id\n" +
                "                JOIN tenmo_user u1 ON a1.user_id = u1.user_id\n" +
                "                JOIN tenmo_user u2 ON a2.user_id = u2.user_id\n" +
                "                WHERE transfer_status_id = 1 AND account_from = (select account_id from account where user_id = ?) OR account_to = (select account_id from account where user_id = ?)";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            pending.add(transfer);
        }
        return pending;
    }

    @Override
    public Transfer getTransferById( int transferId ) {
        Transfer transfer = new Transfer();
        String sql = "SELECT t.*, u1.username AS userFrom, u2.username AS userTo, ts.transfer_status_desc, tt.transfer_type_desc FROM transfer t " +
                "JOIN account a1 ON t.account_from = a1.account_id " +
                "JOIN account a2 ON t.account_to = a2.account_id " +
                "JOIN tenmo_user u1 ON a1.user_id = u1.user_id " +
                "JOIN tenmo_user u2 ON a2.user_id = u2.user_id " +
                "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id " +
                "JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id " +
                "WHERE t.transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public String sendBucks( int accountFrom, int accountTo, BigDecimal amount ) {
        if (accountFrom == accountTo) {
            return "User can't send money to self";
        }
        if (amount.compareTo(accountDao.getBalance(accountFrom)) == -1 && amount.compareTo(new BigDecimal(0)) == 1) {
            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (2, 2, (select account_id from account where user_id = ?),( select account_id from account where user_id = ?), ?)";
            jdbcTemplate.update(sql, accountFrom, accountTo, amount);
            accountDao.addToBalance(amount, accountTo);
            accountDao.subtractFromBalance(amount, accountFrom);
            return "Transfer complete";
        } else {
            return "Transfer failed, you're probably broke like me";
        }
    }


    @Override
    public String requestBucks( int accountTo, int accountFrom, BigDecimal amount ) {
            if (accountTo == accountFrom) {
                return "You can not request money from your self.";
            }
            if (amount.compareTo(new BigDecimal(0)) == 1) {
                String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                        "VALUES (1, 1, (select account_id from account where user_id = ?),( select account_id from account where user_id = ?), ?)";
                jdbcTemplate.update(sql, accountFrom, accountTo, amount);
                return "Request sent";
            } else {
                return "There was a problem sending the request";
            }
        }

    @Override
    public String updateTransferRequest( Transfer transfer, int transferStatusId ) {
        if (transferStatusId == 3) {
            String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
            jdbcTemplate.update(sql, transferStatusId, transfer.getTransferId());
            return "Update successful";
        }
        if (!(accountDao.getBalance(transfer.getAccountFrom()).compareTo(transfer.getAmount()) == -1)) {
            String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
            jdbcTemplate.update(sql, transferStatusId, transfer.getTransferId());
            accountDao.addToBalance(transfer.getAmount(), transfer.getAccountTo());
            accountDao.subtractFromBalance(transfer.getAmount(), transfer.getAccountFrom());
            return "Update successful";
        } else {
            return "NO MONEY for transfer";
        }
    }

    private Transfer mapRowToTransfer(SqlRowSet results){
            Transfer transfer = new Transfer();
            transfer.setTransferId(results.getInt("transfer_id"));
            transfer.setTransferTypeId(results.getInt("transfer_type_id"));
            transfer.setTransferStatusId(results.getInt("transfer_status_id"));
            transfer.setAccountFrom(results.getInt("account_from"));
            transfer.setAccountTo(results.getInt("account_to"));
            transfer.setAmount(results.getBigDecimal("amount"));
            try {
                transfer.setUserFrom(results.getString("userFrom"));
                transfer.setUserTo(results.getString("userTo"));
            } catch (Exception e) {
            }
//            try {
//                transfer.setTransferType(results.getString("transfer_type_desc"));
//                transfer.setTransferStatus(results.getString("transfer_status_desc"));
//            } catch (Exception e) {
//            }
            return transfer;
    }

    public void setAccountDao (AccountDao accountDao ){
            this.accountDao = accountDao;
    }
}
