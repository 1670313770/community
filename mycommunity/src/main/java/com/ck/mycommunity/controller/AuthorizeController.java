package com.ck.mycommunity.controller;

import com.ck.mycommunity.domain.User;
import com.ck.mycommunity.pojo.AccessTokenPojo;
import com.ck.mycommunity.pojo.GitConfigMessagePojo;
import com.ck.mycommunity.pojo.GithubUser;
import com.ck.mycommunity.provider.GithubProvider;
import com.ck.mycommunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author CK
 * @create 2020-01-23-16:07
 */
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private GitConfigMessagePojo gitConfigMessagePojo;

    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(
            @RequestParam(name = "code")String code,
            @RequestParam(name = "state")String state,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        AccessTokenPojo accessTokenPojo = new AccessTokenPojo();
        accessTokenPojo.setCode(code);
        accessTokenPojo.setState(state);
        accessTokenPojo.setRedirect_uri(gitConfigMessagePojo.getRedirect());
        accessTokenPojo.setClient_id(gitConfigMessagePojo.getId());
        accessTokenPojo.setClient_secret(gitConfigMessagePojo.getSecret());
        String accessToken = githubProvider.getAccessToken(accessTokenPojo);
        GithubUser githubUser = githubProvider.githubUser(accessToken);
        if(githubUser!=null){//登录成功
//            添加进数据库
            User user=new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatar_url());
            userService.insertUser(user);
//            写入Cookie
            response.addCookie(new Cookie("token",user.getToken()));
            return "redirect:/";
        }else {//登录失败
            return "redirect:/";
        }

    }

}
