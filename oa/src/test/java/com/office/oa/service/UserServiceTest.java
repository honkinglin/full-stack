package com.office.oa.service;

import com.office.oa.entity.User;
import org.junit.Test;

public class UserServiceTest {
    private UserService userService = new UserService();

    @Test
    public void checkLogin() {
        User user = userService.checkLogin("m8", "test");
//        User user = userService.checkLogin("m8", "test1");
        System.out.println(user);
    }
}