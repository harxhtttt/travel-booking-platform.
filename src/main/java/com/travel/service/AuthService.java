package com.travel.service;

import com.travel.dao.UserDao;
import com.travel.exception.UnauthorizedActionException;
import com.travel.model.Traveler;
import com.travel.model.User;
import com.travel.util.PasswordUtil;

public class AuthService {
    private final UserDao userDao = new UserDao();

    public User register(String name, String email, String password) throws Exception {
        if (userDao.findByEmail(email) != null) {
            throw new Exception("Email already registered.");
        }
        String hash = PasswordUtil.hashPassword(password);
        Traveler traveler = new Traveler(0, name, email, hash);
        userDao.save(traveler);
        return traveler;
    }

    public User login(String email, String password) throws UnauthorizedActionException {
        User user = userDao.findByEmail(email);
        if (user == null || !PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            throw new UnauthorizedActionException("Invalid email or password.");
        }
        return user;
    }
}
