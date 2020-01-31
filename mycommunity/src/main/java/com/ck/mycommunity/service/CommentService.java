package com.ck.mycommunity.service;

import com.ck.mycommunity.dao.CommentDao;
import com.ck.mycommunity.dao.NoticeDao;
import com.ck.mycommunity.dao.QuestionDao;
import com.ck.mycommunity.dao.UserDao;
import com.ck.mycommunity.domain.Comment;
import com.ck.mycommunity.domain.Notice;
import com.ck.mycommunity.domain.Question;
import com.ck.mycommunity.domain.User;
import com.ck.mycommunity.enums.CommentTypeEnum;
import com.ck.mycommunity.exception.CustomizeErrorCode;
import com.ck.mycommunity.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author CK
 * @create 2020-01-29-20:42
 */
@Service
public class CommentService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private NoticeDao noticeDao;

    @Transactional
    public void addComment(Comment comment){
        //回复的问题不能为空
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        //回复的类型必须是为一级和二级
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        //如果是一级回复
        if (comment.getType() == CommentTypeEnum.QUESTION.getType()) {
            //查找-问题
            Question question = questionDao.selectByPrimaryKey(comment.getParentId());
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            comment.setCommentCount(0);
            commentDao.insert(comment);
            question.setCommentCount(question.getCommentCount()+1);
            questionDao.updateByPrimaryKey(question);

            //通知
            Notice notice=new Notice();
            notice.setUid(Integer.parseInt(comment.getCommentator().toString()));
            notice.setQid(question.getId());
            User user = userDao.selectByPrimaryKey(notice.getUid());
            notice.setUname(user.getName());
            notice.setQname(question.getTitle());
            notice.setGmtCreate(comment.getGmtCreate());
            notice.setState(0);
            noticeDao.insert(notice);
        }
        //二级回复
        else {
            Comment fcomment = commentDao.selectByPrimaryKey(comment.getParentId());
            if(fcomment==null)
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);

            Question question = questionDao.selectByPrimaryKey(fcomment.getParentId());
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentDao.insert(comment);

            //更新一级回复的评论是
            fcomment.setCommentCount(fcomment.getCommentCount()+1);
            commentDao.updateByPrimaryKey(fcomment);

            //通知
            Notice notice=new Notice();
            notice.setUid(Integer.parseInt(comment.getCommentator().toString()));
            notice.setQid(question.getId());
            User user = userDao.selectByPrimaryKey(notice.getUid());
            notice.setUname(user.getName());
            notice.setQname(question.getTitle());
            notice.setGmtCreate(comment.getGmtCreate());
            notice.setState(0);
            noticeDao.insert(notice);
        }

    }

}
