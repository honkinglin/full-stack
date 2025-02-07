package com.fullstack.mall.service;

import com.fullstack.mall.exception.MallException;
import com.fullstack.mall.model.pojo.User;

public interface UserService {
    void register(String username, String password, String emailAddress) throws Exception;

    User login(String username, String password) throws MallException;

    void updateInformation(User user) throws MallException;

    boolean checkAdminRole(User user);

    boolean checkEmailRegistered(String emailAddress);

    Boolean checkEmailAndCode(String emailAddress, String verificationCode);
}
