package com.ck.mycommunity.controller;

import com.ck.mycommunity.domain.Notice;
import com.ck.mycommunity.enums.CommentTypeEnum;
import com.ck.mycommunity.pojo.CommentPojo;
import com.ck.mycommunity.pojo.QuestionUserPojo;
import com.ck.mycommunity.service.NoticeService;
import com.ck.mycommunity.service.QuestionService;
import com.ck.mycommunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author CK
 * @create 2020-01-28-20:18
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    @Autowired
    private NoticeService noticeService;

    @GetMapping("/question/{id}")
    public String question(
            @PathVariable(name = "id")Long id, Model model,
            @RequestParam(required = false,name = "nid")Long nid,
            @RequestParam(required = false,name = "state")Integer state
    ){
        questionService.addView(id);
        QuestionUserPojo questionUserById = questionService.findQuestionUserById(Integer.parseInt(id.toString()));
        model.addAttribute("question",questionUserById);
        List<QuestionUserPojo> qusRelated = questionService.findRelated(questionUserById);
        model.addAttribute("relatedQuestions",qusRelated);
        List<CommentPojo> commentPojoById = questionService.findCommentPojoById(id, CommentTypeEnum.QUESTION);
        model.addAttribute("comments",commentPojoById);

        if(nid!=null&&state==0){
            noticeService.AlterNoticeStateByid(nid);
        }
        return "question";
    }

}
