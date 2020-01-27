package com.ck.mycommunity.controller;

import com.ck.mycommunity.domain.Question;
import com.ck.mycommunity.domain.User;
import com.ck.mycommunity.service.QuestionService;
import com.ck.mycommunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author CK
 * @create 2020-01-25-22:00
 */
@Controller
public class PublishController {

    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String publishform(Question question, HttpServletRequest request, Model model){
        User user=null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if("token".equalsIgnoreCase(cookie.getName())){
                user = userService.findByToken(cookie.getValue());
                if(user!=null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }

        if(question.getTitle()==null||"".equalsIgnoreCase(question.getTitle())){
            model.addAttribute("error","标题不能为空");
        }else{
            model.addAttribute("title",question.getTitle());
        }
        if(question.getDescription()==null||"".equalsIgnoreCase(question.getDescription())){
            model.addAttribute("error","问题补充不能为空");
        }else {
            model.addAttribute("description",question.getDescription());
        }
        if(question.getTag()==null||"".equalsIgnoreCase(question.getTag())){
            model.addAttribute("error","标签不能为空");
        }else {
            model.addAttribute("tag",question.getTag());
        }
        if(model.containsAttribute("error"))
            return "publish";

        if(user==null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }else if(user.getId()!=null){
            question.setCreator(user.getId());
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionService.insertQuestion(question);
            System.out.println(question);
        }

        return "redirect:/";
    }
}
