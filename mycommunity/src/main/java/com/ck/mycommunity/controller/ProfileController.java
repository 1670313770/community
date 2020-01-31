package com.ck.mycommunity.controller;

import com.ck.mycommunity.domain.Notice;
import com.ck.mycommunity.domain.User;
import com.ck.mycommunity.pojo.QuestionUserPojo;
import com.ck.mycommunity.service.NoticeService;
import com.ck.mycommunity.service.QuestionService;
import com.ck.mycommunity.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author CK
 * @create 2020-01-28-14:23
 */
@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    @Autowired
    private NoticeService noticeService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action")String action, Model model,
                          @RequestParam(name = "pagNum",defaultValue = "1")String pagNum,
                          @RequestParam(name = "countQuestion",defaultValue = "10")String countQuestion,
                          HttpServletRequest request
    ){
        //获取用户信息
        User user= (User) request.getSession().getAttribute("user");
        if(user==null)
            return "redirect:/";

        if("questions".equalsIgnoreCase(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            //获取该用户提问
            if(user==null){
                return "redirect:/";
            }else {
                PageInfo pageInfo = questionService.findAllQuestionUserPojoByAidWithPage(Integer.parseInt(pagNum), Integer.parseInt(countQuestion),user.getId().toString());
                List<QuestionUserPojo> allQuestionUserPojo = pageInfo.getList();
                model.addAttribute("questions",allQuestionUserPojo);
                model.addAttribute("pageInf",pageInfo);
            }

        }else if("replies".equalsIgnoreCase(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
            if(user==null) {
                return "redirect:/";
            }else{
                PageInfo pageInfo = noticeService.findNoticeByUid(user.getId(), Integer.parseInt(pagNum));
                List<Notice> list = pageInfo.getList();
                model.addAttribute("notifications",list);
                model.addAttribute("pageInf",pageInfo);
            }
        }

        return "profile";
    }

}
