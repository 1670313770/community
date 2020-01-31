package com.ck.mycommunity.pojo;

import com.ck.mycommunity.domain.User;
import lombok.Data;

/**
 * @author CK
 * @create 2020-01-30-15:48
 */
@Data
public class CommentPojo {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private Integer commentCount;
    private String content;
    private User user;
}