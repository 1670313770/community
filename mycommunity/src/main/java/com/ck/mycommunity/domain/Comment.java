package com.ck.mycommunity.domain;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author CK
 * @create 2020-01-29-20:16
 */
@Data
@Table(name = "comment")
public class Comment {

    @Id
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Integer commentCount;

}
