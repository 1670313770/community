package com.ck.mycommunity.pojo;

import lombok.Data;

/**
 * @author CK
 * @create 2020-01-23-17:01
 */
@Data
public class GithubUser {

    private String name;
    private Long id;
    private String bio;
    private String avatar_url;//头像
}
