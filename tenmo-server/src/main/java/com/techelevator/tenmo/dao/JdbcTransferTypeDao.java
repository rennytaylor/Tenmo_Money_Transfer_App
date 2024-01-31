package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferTypeDao implements TransferTypeDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferTypeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TransferType getTransferTypeByTypeID(int transferTypeID) {
        String sql = "SELECT transfer_type_desc, transfer_type_id FROM transfer_type WHERE transfer_type_id = ?";
        TransferType transferType = null;

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferTypeID);
            if (results.next()) {
                transferType = mapRowToTransferType(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferType;
    }

    private TransferType mapRowToTransferType(SqlRowSet rs) {
        TransferType transferType = new TransferType();
        transferType.setTypeID(rs.getInt("transfer_type_id"));
        transferType.setTypeDesc(rs.getString("transfer_type_desc"));
        return transferType;
    }
}
