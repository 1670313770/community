package com.ck.mycommunity.advice;

import com.ck.mycommunity.exception.CustomizeErrorCode;
import com.ck.mycommunity.exception.CustomizeException;
import com.ck.mycommunity.pojo.ResultPojo;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author CK
 * @create 2020-01-29-15:54
 */
@ControllerAdvice
@Slf4j
public class UseNullExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, HttpServletResponse response, Throwable e, Model model){
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            ResultPojo resultDTO;
            // 返回 JSON
            if (e instanceof CustomizeException) {
                resultDTO = ResultPojo.errorOf((CustomizeException) e);
            } else {
                log.error("handle error", e);
                resultDTO = ResultPojo.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(new Gson().toJson(resultDTO));
                writer.close();
            } catch (IOException ioe) {
            }
            return null;
        } else {
            // 错误页面跳转
            if (e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
            } else {
                log.error("handle error", e);
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }

}
