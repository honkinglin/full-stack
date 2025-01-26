package com.office.oa.service;

import com.office.oa.entity.User;
import com.office.oa.mapper.UserMapper;
import com.office.oa.service.exception.LoginException;
import com.office.oa.utils.Md5Utils;

public class UserService {
    private UserMapper userMapper = new UserMapper();

    public User checkLogin(String username, String password) {
        User user = userMapper.selectUserByUsername(username);
        if (user == null) {
            throw new LoginException("User not found");
        }
        String md5 = Md5Utils.md5Digest(password, user.getSalt());
        if (!md5.equals(user.getPassword())) {
            throw new LoginException("Password incorrect");
        }
        return user;
    }
}
