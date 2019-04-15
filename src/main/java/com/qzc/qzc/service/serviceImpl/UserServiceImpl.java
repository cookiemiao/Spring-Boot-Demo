package com.qzc.qzc.service.serviceImpl;

import com.qzc.qzc.bean.User;
import com.qzc.qzc.dao.UserDao;
import com.qzc.qzc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Override
    public User getUser(User u) {
        return userDao.getUserById(u);
    }
}
