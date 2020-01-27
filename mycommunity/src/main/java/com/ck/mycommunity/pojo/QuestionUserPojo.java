package com.ck.mycommunity.pojo;

import com.ck.mycommunity.domain.User;
import lombok.Data;

/**
 * @author CK
 * @create 2020-01-27-12:33
 */
@Data
public class QuestionUserPojo {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
