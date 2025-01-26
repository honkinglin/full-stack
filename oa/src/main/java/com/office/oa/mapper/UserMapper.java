package com.office.oa.mapper;

import com.office.oa.entity.User;
import com.office.oa.utils.MybatisUtils;

public class UserMapper {
    public User selectUserByUsername(String username) {
        User user = (User) MybatisUtils.executeQuery(sqlSession -> sqlSession.selectOne("userMapper.selectByUsername", username));
        return user;
    }
}
