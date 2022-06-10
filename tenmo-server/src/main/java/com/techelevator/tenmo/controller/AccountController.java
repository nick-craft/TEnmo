package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;

@RestController
public class AccountController {

    private AccountDao dao;

//    public AccountController() {this.dao = new JdbcAccountDao(new JdbcTemplate());
//    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Account getAccounts(@PathVariable int userId) throws AccountNotFoundException {
        return dao.get(userId);
    }


}
