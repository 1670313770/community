package com.ck.mycommunity.controller;

import com.ck.mycommunity.domain.User;
import com.ck.mycommunity.pojo.AccessTokenPojo;
import com.ck.mycommunity.pojo.GitConfigMessagePojo;
import com.ck.mycommunity.pojo.GithubUser;
import com.ck.mycommunity.provider.GithubProvider;
import com.ck.mycommunity.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
//          添加进数据库
//            查看数据库是否有该账号
            User hasUser = userService.findUserByAccountID(String.valueOf(githubUser.getId()));
            if(hasUser==null){
                hasUser=new User();
                hasUser.setToken(UUID.randomUUID().toString());
                hasUser.setName(githubUser.getName());
                hasUser.setAccountId(String.valueOf(githubUser.getId()));
                hasUser.setGmtCreate(System.currentTimeMillis());
                hasUser.setGmtModified(hasUser.getGmtCreate());
                hasUser.setAvatarUrl(githubUser.getAvatar_url());
                userService.insertUser(hasUser);
            }else {
                hasUser.setToken(UUID.randomUUID().toString());
                hasUser.setGmtModified(System.currentTimeMillis());
                userService.updateUser(hasUser);
            }
//            写入Cookie
            Cookie cookie = new Cookie("token", hasUser.getToken());
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            response.addCookie(cookie);
            request.getSession().setAttribute("user",hasUser);
            return "redirect:/";
        }else {//登录失败
            log.error("callback get github error,{}", githubUser);
            return "redirect:/";
        }

    }

}
