package com.ck.mycommunity.service;

import com.ck.mycommunity.dao.CommentDao;
import com.ck.mycommunity.dao.QuestionDao;
import com.ck.mycommunity.dao.UserDao;
import com.ck.mycommunity.domain.Comment;
import com.ck.mycommunity.domain.Question;
import com.ck.mycommunity.domain.User;
import com.ck.mycommunity.enums.CommentTypeEnum;
import com.ck.mycommunity.exception.CustomizeErrorCode;
import com.ck.mycommunity.exception.CustomizeException;
import com.ck.mycommunity.pojo.CommentPojo;
import com.ck.mycommunity.pojo.QuestionUserPojo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private CommentDao commentDao;

    public void insertQuestion(Question question){
        if(question.getViewCount()==null)
            question.setViewCount(0);
        if(question.getCommentCount()==null)
            question.setCommentCount(0);
        if(question.getLikeCount()==null)
            question.setLikeCount(0);
        questionDao.insert(question);
    }

    public List<Question> findAll(String search){
        Example example=new Example(Question.class);
        example.setOrderByClause("gmt_create desc");
        if(search!=null&&!"".equalsIgnoreCase(search))
            example.createCriteria().andLike("title","%"+search+"%");
        List<Question> questions = questionDao.selectByExample(example);
        return questions;
    }

    public List<QuestionUserPojo> findAllQuestionUserPojo(String search){
        List<Question> qs = findAll(search);
        if(qs == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        List<QuestionUserPojo> qus = quToQuU(qs);
        return qus;
    }

    public Question findQuestionById(Long id){
        Example example=new Example(Question.class);
        example.createCriteria().andEqualTo("id",id);
        Question question = questionDao.selectOneByExample(example);
        return question;
    }

    public PageInfo findAllWithPage(String search,int nowPage,int countQuestion){
//        分页
        Page<QuestionUserPojo> p = PageHelper.startPage(nowPage,countQuestion);
        List<QuestionUserPojo> qus = findAllQuestionUserPojo(search);
        PageInfo<QuestionUserPojo> questionUserPojoPageInfo = new PageInfo<>(p.getResult());
        questionUserPojoPageInfo.setList(qus);
        return questionUserPojoPageInfo;
    }

    //根据用户分页查询
    public List<QuestionUserPojo> findAllQuestionUserPojoByAid(String uid){
        Example example=new Example(Question.class);
        example.createCriteria().andEqualTo("creator",uid);
        List<Question> qs = questionDao.selectByExample(example);
        if(qs == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        List<QuestionUserPojo> qus = quToQuU(qs);
        return qus;
    }
    //根据用户分页
    public PageInfo findAllQuestionUserPojoByAidWithPage(int nowPage,int countQuestion,String aid){
//        分页
        Page<QuestionUserPojo> p = PageHelper.startPage(nowPage,countQuestion);
        List<QuestionUserPojo> qus = findAllQuestionUserPojoByAid(aid);
        if(qus == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        PageInfo<QuestionUserPojo> questionUserPojoPageInfo = new PageInfo<>(p.getResult());
        questionUserPojoPageInfo.setList(qus);
        return questionUserPojoPageInfo;
    }

    public QuestionUserPojo findQuestionUserById(Integer id){
        Example example = new Example(Question.class);
        example.createCriteria().andEqualTo("id",id);
        List<Question> questions = questionDao.selectByExample(example);
        if(questions == null)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
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

    public void updateQuestionById(Question question) {
        questionDao.updateByPrimaryKey(question);
    }

    @Transactional
    public void addView(Long id) {
        Question questionById = findQuestionById(id);
        if(questionById!=null)
        questionById.setViewCount(questionById.getViewCount()+1);
        questionDao.updateByPrimaryKey(questionById);
    }

    //返回一级、二级回复
    public List<CommentPojo> findCommentPojoById(Long qid, CommentTypeEnum typeEnum){
        //获取当前问题的所有回复
        Example example=new Example(Comment.class);
        example.createCriteria()
                .andEqualTo("parentId",qid)
                .andEqualTo("type",typeEnum.getType());
        example.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentDao.selectByExample(example);
        if(comments==null||comments.size()==0)
            comments=new ArrayList<>();

        //获取当前问题下的所有用户id，且不重复
        Set<Long> set = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> list=new ArrayList<>();
        list.addAll(set);

        //根据id获取所有用户信息
        Map<Long, User> userMap=new HashMap<>();
        for (Long integer : list) {
            User user = userDao.selectByPrimaryKey(integer);
            userMap.put(integer,user);
        }
        //将Commnet类转换为CommentPojo
        List<CommentPojo> collect = comments.stream().map(comment -> {
            CommentPojo commentPojo = new CommentPojo();
            BeanUtils.copyProperties(comment, commentPojo);
            commentPojo.setUser(userMap.get(commentPojo.getCommentator()));
            return commentPojo;
        }).collect(Collectors.toList());

        return collect;
    }

    public List<QuestionUserPojo> findRelated(QuestionUserPojo pojo){
        if(pojo.getTag()==null||"".equalsIgnoreCase(pojo.getTag()))
            return new ArrayList<>();

        String[] split = pojo.getTag().split(",");

        Example example=new Example(Question.class);
        Example.Criteria criteria = example.createCriteria();
        for (String s : split) {
            criteria.orLike("tag","%"+s+"%");
        }
//        example.createCriteria().andNotEqualTo("id",pojo.getId());
        List<Question> questions = questionDao.selectByExample(example);
        for (Question question : questions) {
            if(question.getId()==pojo.getId()){
                questions.remove(question);
                break;
            }
        }
        List<QuestionUserPojo> qus = quToQuU(questions);
        return qus;
    }
}
