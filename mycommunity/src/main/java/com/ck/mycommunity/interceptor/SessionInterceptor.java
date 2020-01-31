package com.ck.mycommunity.interceptor;

import com.ck.mycommunity.domain.User;
import com.ck.mycommunity.service.NoticeService;
import com.ck.mycommunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author CK
 * @create 2020-01-28-17:31
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Autowired
    private NoticeService noticeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();
        if (cookies!=null&&cookies.length!=0)
            for (Cookie cookie : cookies) {
                if("token".equalsIgnoreCase(cookie.getName())){
                    User user = userService.findByToken(cookie.getValue());
                    if(user!=null){
                        Integer unreadCount = noticeService.findNoReadNoticeCount(user.getId());
                        request.getSession().setAttribute("user",user);
                        request.getSession().setAttribute("unreadCount",unreadCount);
                    }
                    break;
                }
            }

        return true;
    }
}
