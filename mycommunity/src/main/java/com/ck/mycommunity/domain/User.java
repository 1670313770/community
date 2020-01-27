package com.ck.mycommunity.domain;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.Mapping;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author CK
 * @create 2020-01-25-15:11
 */
@Data
@Table(name = "gituser")
public class User {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;//头像

}
