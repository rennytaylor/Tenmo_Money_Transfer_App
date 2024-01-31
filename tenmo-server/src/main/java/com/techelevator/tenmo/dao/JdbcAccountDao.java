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

@Component
public class JdbcAccountDao implements AccountDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public BigDecimal getAccountBalanceByUserID(int userID) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userID);

        BigDecimal balance = new BigDecimal("0");
        if (results.next()) {
            balance = results.getBigDecimal("balance");
        }
        return balance;
    }
    @Override
    public Account getAccountByUserID(int userID) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userID);
        Account account = null;
        if (result.next()) {
            account = mapRowToAccount(result);
        }
        return account;
    }

    @Override
    public Account getAccountByAccountId(int accountId) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        Account account = null;
        if (result.next()) {
            account = mapRowToAccount(result);
        }
        return account;
    }

//    @Override
//    public int getAccountByUserID(int userID){
//        String sql = "SELECT account_id FROM account WHERE user_id = ?";
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userID);
//
//        int accountId = 0;
//        if (results.next()) {
//            accountId = results.getInt("account_id");
//        }
//        return accountId;
//    }

    @Override
    public Account updateBalance(Account account){
        Account updatedAccount;
        String sql = "UPDATE account SET balance = ? WHERE user_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, account.getBalance(), account.getUserID());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            updatedAccount = getAccountByUserID(account.getUserID());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        } return updatedAccount;
    }

    @Transactional
    @Override
    public void updateAccountBalances(Transfer transfer) {
        String sql1 = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        String sql2 =  "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        try {
            int numberOfRows1 = jdbcTemplate.update(sql1, transfer.getAmount(), transfer.getAccountFrom());
            if (numberOfRows1 == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            int numberOfRows2 = jdbcTemplate.update(sql2, transfer.getAmount(), transfer.getAccountTo());
            if (numberOfRows2 == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setUserID(rs.getInt("user_id"));
        account.setAccountID(rs.getInt("account_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}
