package com.ck.mycommunity.domain;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author CK
 * @create 2020-01-31-12:49
 */
@Data
@Table(name = "notice")
public class Notice {
    @Id
    private Long id;
    private Integer uid;
    private Integer qid;
    private String uname;
    private String qname;
    private Long gmtCreate;
    private Integer state;
}
