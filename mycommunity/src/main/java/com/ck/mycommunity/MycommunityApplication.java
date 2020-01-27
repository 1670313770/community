package com.ck.mycommunity;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.ck.mycommunity.dao")
public class MycommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(MycommunityApplication.class, args);
	}
}
