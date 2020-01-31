package com.ck.mycommunity.controller;

import com.ck.mycommunity.domain.Comment;
import com.ck.mycommunity.domain.User;
import com.ck.mycommunity.enums.CommentTypeEnum;
import com.ck.mycommunity.exception.CustomizeErrorCode;
import com.ck.mycommunity.exception.CustomizeException;
import com.ck.mycommunity.pojo.CommentPojo;
import com.ck.mycommunity.pojo.ResultPojo;
import com.ck.mycommunity.service.CommentService;
import com.ck.mycommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CK
 * @create 2020-01-29-20:42
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private QuestionService questionService;

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

        return ResultPojo.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultPojo<List<CommentPojo>> SecComments(@PathVariable(name = "id")Long id){
        List<CommentPojo> commentPojoById = questionService.findCommentPojoById(id, CommentTypeEnum.COMMENT);
        return ResultPojo.okOf(commentPojoById);
    }
}
