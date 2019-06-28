package com.qzc.qzc.controller;

import com.qzc.qzc.bean.User;
import com.qzc.qzc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "user")
public class UserController {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @RequestMapping("getuser")
    public User getUser(User user) {
//        logger.info("根据id取得user");
        try {
            user = userService.getUser(user);
            return user;
        } catch (Exception e) {
            logger.error("取得user失败！", e);
//            e.printStackTrace();
            return null;
        }
    }

    public User getRawData(Object... objects) {
        User user = (User) objects[0];
        user = userService.getUser(user);
        return user;
    }

}
