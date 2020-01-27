package com.ck.mycommunity.controller;

import com.ck.mycommunity.domain.User;
import com.ck.mycommunity.pojo.QuestionUserPojo;
import com.ck.mycommunity.service.QuestionService;
import com.ck.mycommunity.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author CK
 * @create 2020-01-22-21:28
 */
@Controller
public class IndexController {

    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;

    @RequestMapping("/")
    public String index(
            HttpServletRequest request, Model model,
            @RequestParam(name = "pagNum",defaultValue = "1")String pagNum,
            @RequestParam(name = "countQuestion",defaultValue = "10")String countQuestion
    ){
        Cookie[] cookies = request.getCookies();
        if (cookies!=null&&cookies.length!=0)
        for (Cookie cookie : cookies) {
           if("token".equalsIgnoreCase(cookie.getName())){
               User user = userService.findByToken(cookie.getValue());
               if(user!=null){
                   request.getSession().setAttribute("user",user);
               }
               break;
           }
        }
        PageInfo pageInfo = questionService.findAllWithPage(Integer.parseInt(pagNum), Integer.parseInt(countQuestion));
        List<QuestionUserPojo> allQuestionUserPojo = pageInfo.getList();
        model.addAttribute("questions",allQuestionUserPojo);
        model.addAttribute("pageInf",pageInfo);
        return "index";
    }
}
