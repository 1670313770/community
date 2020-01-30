package com.ck.mycommunity.service;

import com.ck.mycommunity.dao.UserDao;
import com.ck.mycommunity.domain.User;
import com.ck.mycommunity.exception.CustomizeErrorCode;
import com.ck.mycommunity.exception.CustomizeException;
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
        if(users == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        return users;
    }
    public User findUserByAccountID(String aid){
        Example example=new Example(User.class);
        example.createCriteria().andEqualTo("accountId",aid);
        User user1 = userDao.selectOneByExample(example);
        if(user1 == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        return user1;
    }

    public Integer insertUser(User user){
        return userDao.insert(user);
    }

    public void updateUser(User user){
        Example example=new Example(User.class);
        example.createCriteria().andEqualTo("id",user.getId()).andEqualTo("accountId",user.getAccountId());
        userDao.updateByExample(user,example);
    }

    public User findByToken(String token){
        User user=new User();
        user.setToken(token);
        User user1 = userDao.selectOne(user);
        if(user1 == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        return user1;
    }


}
