package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private final JdbcTemplate jdbcTemplate;
    private final AccountDao accountDao;
    private Account account;
    private final UserDao userDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, AccountDao accountDao, UserDao userDao) {  //Spring needs to run the sql request
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }
// these may be needed for testing
//    private final JdbcTemplate jdbcTemplate;
//
//    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE transfer_id = ?";
        Transfer transfer = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }

    //Used this transfer method for reference for getTransfersByAccountId
    //userId is not in the transfer table and I think that's why this one doesn't work.
    @Override
    public List<Transfer> getAllTransfersByUserId(int userId) {
        String sql = "SELECT * FROM transfer WHERE account_from = ? OR account_to = ?";
        List<Transfer> transfers = new ArrayList<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
            while (results.next()) {
                transfers.add(mapRowToTransfer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Transactional
    @Override
    public Transfer updateTransfer(Transfer transfer, int statusId) {
        Transfer updatedTransfer;
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, statusId, transfer.getTransferId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            updatedTransfer = getTransferByTransferId(transfer.getTransferId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedTransfer;
    }

    @Override
    public int deleteTransferById(int transferId) {
        int numberOfRows = 0;
        String sql = "DELETE FROM transfer WHERE transfer_id = ?";
        try {
            numberOfRows =  jdbcTemplate.update(sql, transferId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return numberOfRows;


    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        Transfer transfer1= null;
        String sql = "INSERT INTO transfer(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        try {
            int newTransferId = (jdbcTemplate.queryForObject(sql, int.class, transfer.getTransferTypeId(),
                    transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(),
                    transfer.getAmount()));
            transfer1 = getTransferByTransferId(newTransferId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return transfer1;
    }

    @Transactional
    @Override
    public void updateAccountBalances(int accountFrom, int accountTo, BigDecimal amount) {
        String sql1 = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        String sql2 =  "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        try {
            int numberOfRows1 = jdbcTemplate.update(sql1, amount, accountFrom, amount, accountTo);
            if (numberOfRows1 == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            int numberOfRows2 = jdbcTemplate.update(sql2, amount, accountTo);
            if (numberOfRows2 == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    //Renny's additions for Pending

    @Override
    public List<Transfer> getTransfersByAccountId(int accountId) {
        String sql = "SELECT * FROM transfer WHERE account_from = ? OR account_to = ?;";
        List<Transfer> transfers = new ArrayList<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
            while (results.next()) {
                transfers.add(mapRowToTransfer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }
    //End of Renny's additions
    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setAccountFrom(results.getInt("account_from"));
        transfer.setAccountTo(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        transfer.setTransferStatusId(results.getInt("transfer_status_id"));
        return transfer;
    }
}
