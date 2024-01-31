package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferStatusDao implements TransferStatusDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferStatusDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TransferStatus getTransferStatusDescByStatusID(int statusID) {
        String sql = "SELECT transfer_status_desc, transfer_status_id FROM transfer_status WHERE transfer_status_id = ?";
        TransferStatus transferStatus = null;
        //String transferStatusDesc = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, statusID);
            if (results.next()) {
                transferStatus = mapRowToTransferStatus(results);
                //transferStatusDesc = results.getNString("transfer_status_desc");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferStatus;
    }

    private TransferStatus mapRowToTransferStatus(SqlRowSet rs) {
        TransferStatus transferStatus = new TransferStatus();
        transferStatus.setStatusID(rs.getInt("transfer_status_id"));
        transferStatus.setStatusDesc(rs.getString("transfer_status_desc"));
        return transferStatus;
    }


}
