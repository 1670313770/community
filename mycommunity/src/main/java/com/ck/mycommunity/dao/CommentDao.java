package com.ck.mycommunity.dao;

import com.ck.mycommunity.domain.Comment;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author CK
 * @create 2020-01-29-20:24
 */
@Repository
public interface CommentDao extends Mapper<Comment> {
}
