package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.User;
import org.springframework.data.relational.core.sql.UpdateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserDao userDao;
    public UserController(UserDao userDao){
        this.userDao = userDao;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<User> list(){
        return userDao.getUsers();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public User get(@PathVariable int id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new DaoException("User not found");
        } else {
            return user;
        }
    }

    @RequestMapping(path = "/exist/{id}", method = RequestMethod.GET)
    public User exist(@PathVariable int id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new DaoException("User not found");
        } else {
            return user;
        }
    }
}
