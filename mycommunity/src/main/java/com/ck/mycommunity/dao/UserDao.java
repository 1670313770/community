package com.ck.mycommunity.dao;

import com.ck.mycommunity.domain.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author CK
 * @create 2020-01-25-15:09
 */
@Repository
public interface UserDao extends Mapper<User> {
}
