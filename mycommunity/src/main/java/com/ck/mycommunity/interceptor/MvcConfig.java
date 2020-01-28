package com.ck.mycommunity.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CK
 * @create 2020-01-28-17:28
 */
@Configuration
//@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludeList=new ArrayList<>();
        excludeList.add("/");
        excludeList.add("/callback");
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**").excludePathPatterns(excludeList);
    }
}
