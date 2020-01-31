package com.ck.mycommunity;

import com.ck.mycommunity.domain.User;
import com.ck.mycommunity.enums.CommentTypeEnum;
import com.ck.mycommunity.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MycommunityApplicationTests {

    @Autowired
    private UserService userService;


    @Test
	public void test1() {
        System.out.println(1== CommentTypeEnum.QUESTION.getType());
    }

}
