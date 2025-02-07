package com.fullstack.mall.service.impl;

import com.fullstack.mall.exception.MallException;
import com.fullstack.mall.exception.MallExceptionEnum;
import com.fullstack.mall.model.dao.UserMapper;
import com.fullstack.mall.model.pojo.User;
import com.fullstack.mall.service.UserService;
import com.fullstack.mall.utils.MD5Utils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    public UserMapper userMapper;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 注册
     * @param username
     * @param password
     * @throws MallException
     */
    @Override
    public void register(String username, String password, String emailAddress) throws MallException {
//        查询重名
        User result = userMapper.selectByName(username);
        if (result != null) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
//        写到数据库
        User user = new User();
        user.setUsername(username);
        user.setEmailAddress(emailAddress);
        try {
            user.setPassword(MD5Utils.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        int count = userMapper.insertSelective(user);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.INSERT_FAILED);
        }
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     * @throws MallException
     */
    @Override
    public User login(String username, String password) throws MallException {
        String md5Password = "";
        try {
            md5Password = MD5Utils.getMD5Str(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            throw new MallException(MallExceptionEnum.WRONG_PASSWORD);
        }
        return user;
    }

    /**
     * 更新用户信息
     * @param user
     * @throws MallException
     */
    @Override
    public void updateInformation(User user) throws MallException {
        int count = userMapper.updateByPrimaryKeySelective(user);
        if (count > 1) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    /**
     * 检查是否是管理员
     * @param user
     * @return
     */
    @Override
    public boolean checkAdminRole(User user) {
//        1是普通用户，2是管理员
        return user.getRole().equals(2);
    }

    @Override
    public boolean checkEmailRegistered(String emailAddress) {
        User user = userMapper.selectOneByEmailAddress(emailAddress);
        if (user != null) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean checkEmailAndCode(String emailAddress, String verificationCode) {
        RBucket<String> bucket = redissonClient.getBucket(emailAddress);
        boolean exists = bucket.isExists();
        if (exists) {
            String code = bucket.get();
            //redis里存储的验证码，和用户传过来的一致，则校验通过
            if (code.equals(verificationCode)) {
                return true;
            }
        }
        return false;
    }
}
