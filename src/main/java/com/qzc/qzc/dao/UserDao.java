package com.qzc.qzc.dao;

import com.qzc.qzc.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {
    User getUserById(User user);
}
