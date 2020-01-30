package com.ck.mycommunity.controller;

import com.ck.mycommunity.enums.CommentTypeEnum;
import com.ck.mycommunity.pojo.CommentPojo;
import com.ck.mycommunity.pojo.QuestionUserPojo;
import com.ck.mycommunity.service.QuestionService;
import com.ck.mycommunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Long id, Model model){
        questionService.addView(id);
        QuestionUserPojo questionUserById = questionService.findQuestionUserById(Integer.parseInt(id.toString()));
        model.addAttribute("question",questionUserById);
        List<CommentPojo> commentPojoById = questionService.findCommentPojoById(id, CommentTypeEnum.QUESTION);
        model.addAttribute("comments",questionUserById);
        return "question";
    }

}
