package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;

@RestController
public class AccountController {

    private AccountDao dao;

//    public AccountController() {this.dao = new JdbcAccountDao(new JdbcTemplate());
//    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Account getAccounts(@Valid @RequestBody Account account, @PathVariable int id) {
        return dao.get(id, account.getBalance());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Account create(@Valid @RequestBody Account account, @PathVariable int id, @PathVariable int userId) {
        return dao.create(account, id, userId);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public Account update(@Valid @RequestBody Account account, @PathVariable int id) throws AccountNotFoundException {
        return dao.update(account, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}/{userId}", method =  RequestMethod.DELETE)
    public void delete(@PathVariable int id, @PathVariable int userId) throws AccountNotFoundException {
        dao.delete(id, userId);
    }
}
