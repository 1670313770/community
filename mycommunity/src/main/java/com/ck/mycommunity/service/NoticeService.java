package com.ck.mycommunity.service;

import com.ck.mycommunity.dao.NoticeDao;
import com.ck.mycommunity.domain.Notice;
import com.ck.mycommunity.exception.CustomizeErrorCode;
import com.ck.mycommunity.exception.CustomizeException;
import com.ck.mycommunity.pojo.QuestionUserPojo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author CK
 * @create 2020-01-31-13:16
 */
@Service
public class NoticeService {

    @Autowired
    private NoticeDao noticeDao;

    public PageInfo findNoticeByUid(Integer uid,Integer nowPage){
        Example example=new Example(Notice.class);
        example.createCriteria().andEqualTo("uid",uid);
        example.setOrderByClause("state asc,gmt_create desc");
        Page<Notice> p = PageHelper.startPage(nowPage,10);
        List<Notice> notices = noticeDao.selectByExample(example);
        if(notices == null)
            throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
        PageInfo<Notice> questionUserPojoPageInfo = new PageInfo<>(p.getResult());
        return questionUserPojoPageInfo;
    }

    public Integer findNoReadNoticeCount(Integer uid){
        Example example=new Example(Notice.class);
        example.createCriteria().andEqualTo("uid",uid);
        List<Notice> notices = noticeDao.selectByExample(example);
        Integer sizes=0;
        for (Notice notice : notices) {
            if(notice.getState()==0)
                sizes++;
        }
        return sizes;
    }

    public void AlterNoticeStateByid(Long id){
        Notice notice = noticeDao.selectByPrimaryKey(id);
        if(notice!=null&&notice.getState()==0){
            notice.setState(1);
            noticeDao.updateByPrimaryKey(notice);
        }
    }

}
