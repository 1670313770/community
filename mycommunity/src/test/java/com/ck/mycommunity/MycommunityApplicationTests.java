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
//        User user=new User();
//        user.setAccountId("47101180");
//        user.setId(9);
//        user.setToken("eb1c9524-c030-4fde-932f-3a7c8a6c1c18");
//        user.setName("CK");
//        user.setAvatarUrl("https://avatars1.githubusercontent.com/u/47101180?v=4");
//        userService.updateUser(user);

        System.out.println(1== CommentTypeEnum.QUESTION.getType());
    }

}
