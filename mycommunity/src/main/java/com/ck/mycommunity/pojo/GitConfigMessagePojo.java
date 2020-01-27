package com.ck.mycommunity.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author CK
 * @create 2020-01-23-21:41
 */
@Data
@Component
@ConfigurationProperties(prefix = "git")
public class GitConfigMessagePojo {

    private String id;
    private String secret;
    private String redirect;

}
