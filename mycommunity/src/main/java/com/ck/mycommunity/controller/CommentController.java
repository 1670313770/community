package com.ck.mycommunity.controller;

import com.ck.mycommunity.domain.Comment;
import com.ck.mycommunity.domain.User;
import com.ck.mycommunity.exception.CustomizeErrorCode;
import com.ck.mycommunity.exception.CustomizeException;
import com.ck.mycommunity.pojo.CommentPojo;
import com.ck.mycommunity.service.CommentService;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CK
 * @create 2020-01-29-20:42
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentPojo commentPojo, HttpServletRequest request){
        User user= (User) request.getSession().getAttribute("user");
        if(user==null)
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);

        Comment comment=new Comment();
        comment.setParentId(commentPojo.getParentId());
        comment.setType(commentPojo.getType());
        comment.setContent(commentPojo.getContent());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(Long.valueOf(user.getId().toString()));
        comment.setLikeCount(0L);
        commentService.addComment(comment);

        Map<String,Object> map=new HashMap<>();
        map.put("message","成功");
        return map;
    }

}
