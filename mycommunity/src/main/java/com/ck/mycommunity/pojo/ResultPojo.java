package com.ck.mycommunity.pojo;

import com.ck.mycommunity.exception.CustomizeErrorCode;
import com.ck.mycommunity.exception.CustomizeException;
import lombok.Data;

/**
 * @author CK
 * @create 2020-01-29-21:19
 */
@Data
public class ResultPojo<T> {
    private Integer code;
    private String message;
    private T data;

    public static ResultPojo errorOf(Integer code, String message) {
        ResultPojo resultPojo = new ResultPojo();
        resultPojo.setCode(code);
        resultPojo.setMessage(message);
        return resultPojo;
    }

    public static ResultPojo errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultPojo errorOf(CustomizeException e) {
        return errorOf(e.getCode(), e.getMessage());
    }

    public static ResultPojo okOf() {
        ResultPojo resultPojo = new ResultPojo();
        resultPojo.setCode(200);
        resultPojo.setMessage("请求成功");
        return resultPojo;
    }

    public static <T> ResultPojo okOf(T t) {
        ResultPojo resultPojo = new ResultPojo();
        resultPojo.setCode(200);
        resultPojo.setMessage("请求成功");
        resultPojo.setData(t);
        return resultPojo;
    }
}
