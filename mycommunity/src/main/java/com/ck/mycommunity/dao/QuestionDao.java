package com.ck.mycommunity.dao;

import com.ck.mycommunity.domain.Question;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author CK
 * @create 2020-01-26-21:30
 */
@Repository
public interface QuestionDao extends Mapper<Question> {
}
