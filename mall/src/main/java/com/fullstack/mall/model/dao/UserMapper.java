package com.fullstack.mall.model.dao;

import com.fullstack.mall.model.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User row);

    int insertSelective(User row);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User row);

    int updateByPrimaryKey(User row);

    User selectByName(String username);

    User selectLogin(@Param("username") String username, @Param("password") String password);

    User selectOneByEmailAddress(String emailAddress);
}