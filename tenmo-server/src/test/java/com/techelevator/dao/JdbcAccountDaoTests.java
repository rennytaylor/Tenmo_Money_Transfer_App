//package com.techelevator.dao;
//
//import com.techelevator.tenmo.dao.JdbcAccountDao;
//import com.techelevator.tenmo.dao.JdbcUserDao;
//import com.techelevator.tenmo.model.Account;
//import com.techelevator.tenmo.model.Transfer;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.math.BigDecimal;
//
//public class JdbcAccountDaoTests extends BaseDaoTests {
//
//    Account accountTestOne = new Account(1, 01, new BigDecimal(1000));
//    Account accountTestTwo = new Account(2, 02, new BigDecimal(1000));
//    private JdbcAccountDao test;
//
//    @Before
//    public void setup() {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        test = new JdbcAccountDao(jdbcTemplate);
//    }
//
//    @Test
//    public void getAccountByAccountId_given_valid_Id_returns_account() {
//        Account account = test.getAccountByAccountId(01);
//
//        Assert.assertEquals(accountTestOne, account);
//    }
//
//    @Test
//    public void getAccountByUserId_given_valid_Id_returns_account() {
//        Account account = test.getAccountByUserID(1);
//
//        Assert.assertEquals(accountTestOne, account);
//    }
//
//    @Test
//    public void getAccountBalanceByUserId_given_valid_Id_returns_account() {
//        BigDecimal balance = test.getAccountBalanceByUserID(1);
//
//        Assert.assertEquals(1000, balance);
//    }
//
//    //public void updateAccountBalances_changes_account_balances() {}
//}