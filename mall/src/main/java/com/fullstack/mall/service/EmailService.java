package com.fullstack.mall.service;

/**
 * 描述：     邮件Service
 */

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

    Boolean saveEmailToRedis(String emailAddress, String verificationCode);

    Boolean checkEmailAndCode(String emailAddress, String verificationCode);
}
