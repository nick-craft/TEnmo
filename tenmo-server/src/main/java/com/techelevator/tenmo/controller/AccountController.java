package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


    @RestController
//    @CrossOrigin(origins = "localhost:8080")
    @PreAuthorize("permitAll")
    public class AccountController {

        @Autowired
        private final AccountDao accountDao;
        @Autowired
        private final UserDao userDAO;


        public AccountController( AccountDao accountDao, UserDao userDao ) {
            this.accountDao = accountDao;
            this.userDAO = userDao;
        }


        @RequestMapping(path = "balance/{id}", method = RequestMethod.GET)
        public BigDecimal getBalance( @PathVariable int id ) {
            return accountDao.getBalance(id);
        }

        @RequestMapping(path = "listusers", method = RequestMethod.GET)
        public List<User> listUsers() {
            return userDAO.findAll();
        }
    }

