package com.ck.mycommunity.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author CK
 * @create 2020-01-23-16:23
 */

@Data
public class AccessTokenPojo {

    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;

}
