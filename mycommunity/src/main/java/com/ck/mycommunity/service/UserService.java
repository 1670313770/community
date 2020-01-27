package com.ck.mycommunity.service;

import com.ck.mycommunity.dao.UserDao;
import com.ck.mycommunity.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author CK
 * @create 2020-01-25-15:30
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public List<User> findAll(){
        List<User> users = userDao.selectAll();
        return users;
    }

    public Integer insertUser(User user){
        return userDao.insert(user);
    }

    public User findByToken(String token){
        User user=new User();
        user.setToken(token);
         return userDao.selectOne(user);
    }

}
