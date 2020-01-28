package com.ck.mycommunity.service;

import com.ck.mycommunity.dao.QuestionDao;
import com.ck.mycommunity.dao.UserDao;
import com.ck.mycommunity.domain.Question;
import com.ck.mycommunity.domain.User;
import com.ck.mycommunity.pojo.QuestionUserPojo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CK
 * @create 2020-01-26-21:32
 */
@Service
public class QuestionService {
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private UserDao userDao;

    public void insertQuestion(Question question){
        questionDao.insert(question);
    }

    public List<Question> findAll(){
        return questionDao.selectAll();
    }

    public List<QuestionUserPojo> findAllQuestionUserPojo(){
        List<Question> qs = findAll();
        List<QuestionUserPojo> qus = quToQuU(qs);
        return qus;
    }

    public PageInfo findAllWithPage(int nowPage,int countQuestion){
//        分页
        Page<QuestionUserPojo> p = PageHelper.startPage(nowPage,countQuestion);
        List<QuestionUserPojo> qus = findAllQuestionUserPojo();
        PageInfo<QuestionUserPojo> questionUserPojoPageInfo = new PageInfo<>(p.getResult());
        questionUserPojoPageInfo.setList(qus);
        return questionUserPojoPageInfo;
    }

    //根据用户分页查询
    public List<QuestionUserPojo> findAllQuestionUserPojoByAid(String uid){
        Example example=new Example(Question.class);
        example.createCriteria().andEqualTo("creator",uid);
        List<Question> qs = questionDao.selectByExample(example);
        List<QuestionUserPojo> qus = quToQuU(qs);
        return qus;
    }
    //根据用户分页
    public PageInfo findAllQuestionUserPojoByAidWithPage(int nowPage,int countQuestion,String aid){
//        分页
        Page<QuestionUserPojo> p = PageHelper.startPage(nowPage,countQuestion);
        List<QuestionUserPojo> qus = findAllQuestionUserPojoByAid(aid);
        PageInfo<QuestionUserPojo> questionUserPojoPageInfo = new PageInfo<>(p.getResult());
        questionUserPojoPageInfo.setList(qus);
        return questionUserPojoPageInfo;
    }

    public QuestionUserPojo findQuestionUserById(Integer id){
        Example example = new Example(Question.class);
        example.createCriteria().andEqualTo("id",id);
        List<Question> questions = questionDao.selectByExample(example);
        List<QuestionUserPojo> questionUserPojos = quToQuU(questions);
        return questionUserPojos.get(0);
    }


    private List<QuestionUserPojo> quToQuU(List<Question> qs){
        List<QuestionUserPojo> qus=new ArrayList<>();
        for (Question q : qs) {
            QuestionUserPojo questionUserPojo=new QuestionUserPojo();
            if(q.getViewCount()==null)
                q.setViewCount(0);
            if(q.getCommentCount()==null)
                q.setCommentCount(0);
            if(q.getLikeCount()==null)
                q.setLikeCount(0);
            BeanUtils.copyProperties(q,questionUserPojo);
            User user = userDao.selectByPrimaryKey(questionUserPojo.getCreator());
            questionUserPojo.setUser(user);
            qus.add(questionUserPojo);
        }
        return qus;
    }
}
