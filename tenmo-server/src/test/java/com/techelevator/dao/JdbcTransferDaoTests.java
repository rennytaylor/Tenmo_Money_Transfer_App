//package com.techelevator.dao;
//
//import com.techelevator.tenmo.dao.JdbcTransferDao;
//import com.techelevator.tenmo.model.Transfer;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.math.BigDecimal;
//
//public class JdbcTransferDaoTests extends BaseDaoTests{
//
//    Transfer transferTestSend = new Transfer(01,2,2,1001,1002, new BigDecimal(100), "Send", "Approved");
//    Transfer transferTestRequestPend = new Transfer(02,1,1,1001,1002, new BigDecimal(100), "Request", "Pending");
//    Transfer transferTestRequestApp = new Transfer(03,1,2,1001,1002, new BigDecimal(100), "Request", "Approved");
//    Transfer transferTestRequestRej = new Transfer(04,1,3,1001,1002, new BigDecimal(100), "Request", "Rejected");
//    private JdbcTransferDao test;
//
//    @Before
//    public void setup() {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        test = new JdbcTransferDao(jdbcTemplate);
//    }
//    @Test
//    public void getTransferByTransferId_given_valid_Id_returns_transfer() {
//       Transfer transfer = test.getTransferByTransferId(01);
//
//        Assert.assertEquals(transferTestSend, transfer);
//    }
//
//    @Test
//    public void getAllTransfersByUserId_given_valid_Id_returns_transfer_list() {
//
//    }
//
//
//}
