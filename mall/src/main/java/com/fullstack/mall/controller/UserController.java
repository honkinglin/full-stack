package com.fullstack.mall.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fullstack.mall.common.ApiRestResponse;
import com.fullstack.mall.common.Constant;
import com.fullstack.mall.exception.MallException;
import com.fullstack.mall.exception.MallExceptionEnum;
import com.fullstack.mall.filter.UserFilter;
import com.fullstack.mall.model.pojo.User;
import com.fullstack.mall.service.EmailService;
import com.fullstack.mall.service.UserService;
import com.fullstack.mall.utils.EmailUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.ExecutorService;

/**
 * 描述：用户 Controller
 */

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    ExecutorService executorService;

    /**
     * 注册
     *
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    @PostMapping("/register")
    public ApiRestResponse register(@RequestParam("userName") String username,
                                    @RequestParam("password") String password,
                                    @RequestParam("emailAddress") String emailAddress,
                                    @RequestParam("verificationCode") String verificationCode) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        if (StringUtils.isEmpty(emailAddress)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_EMAIL_ADDRESS);
        }
        if (StringUtils.isEmpty(verificationCode)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_VERIFICATION_CODE);
        }
        // 如果邮箱已注册，则不允许注册
        if (!userService.checkEmailRegistered(emailAddress)) {
            return ApiRestResponse.error(MallExceptionEnum.EMAIL_ALREADY_BEEN_REGISTERED);
        }
        // 验证邮箱和验证码是否匹配
        if (!emailService.checkEmailAndCode(emailAddress, verificationCode)) {
            return ApiRestResponse.error(MallExceptionEnum.WRONG_VERIFICATION_CODE);
        }

        userService.register(username, password, emailAddress);
        return ApiRestResponse.success();
    }

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @param session
     * @return
     * @throws MallException
     */
    @RequestMapping("/login")
    public ApiRestResponse login(@RequestParam("userName") String username, @RequestParam("password") String password, HttpSession session) throws MallException {
        if (StringUtils.isEmpty(username)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(username, password);
        user.setPassword(null);
        session.setAttribute(Constant.MALL_USER, user);
        return ApiRestResponse.success(user);
    }

    /**
     * 发送邮件
     */
    @PostMapping("/user/sendEmail")
    @ResponseBody
    public ApiRestResponse sendEmail(@RequestParam("emailAddress") String emailAddress)
            throws MallException {
        //检查邮件地址是否有效，检查是否已注册
        boolean validEmailAddress = EmailUtil.isValidEmailAddress(emailAddress);
        if (validEmailAddress) {
            boolean emailPassed = userService.checkEmailRegistered(emailAddress);
            if (!emailPassed) {
                return ApiRestResponse.error(MallExceptionEnum.EMAIL_ALREADY_BEEN_REGISTERED);
            } else {
                String verificationCode = EmailUtil.genVerificationCode();
                Boolean saveEmailToRedis = emailService.saveEmailToRedis(emailAddress, verificationCode);
                if (saveEmailToRedis) {
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            emailService.sendSimpleMessage(emailAddress, Constant.EMAIL_SUBJECT, "欢迎注册，您的验证码是" + verificationCode);
                        }
                    });
                    return ApiRestResponse.success();
                } else {
                    return ApiRestResponse.error(MallExceptionEnum.EMAIL_ALREADY_BEEN_SEND);
                }
            }
        } else {
            return ApiRestResponse.error(MallExceptionEnum.WRONG_EMAIL);
        }
    }

    @GetMapping("/loginWithJwt")
    public ApiRestResponse loginWithJwt(@RequestParam("userName") String username, @RequestParam("password") String password) {
        if (StringUtils.isEmpty(username)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(username, password);
        //保存用户信息时，不保存密码
        user.setPassword(null);
        Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
        String token = JWT.create()
                .withClaim(Constant.USER_NAME, user.getUsername())
                .withClaim(Constant.USER_ID, user.getId())
                .withClaim(Constant.USER_ROLE, user.getRole())
                //过期时间
                .withExpiresAt(new Date(System.currentTimeMillis() + Constant.EXPIRE_TIME))
                .sign(algorithm);
        return ApiRestResponse.success(token);
    }

    /**
     * 管理员登录接口
     */
    @GetMapping("/adminLoginWithJwt")
    public ApiRestResponse adminLoginWithJwt(@RequestParam("userName") String username,
                                             @RequestParam("password") String password)
            throws MallException {
        if (StringUtils.isEmpty(username)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(username, password);
        //校验是否是管理员
        if (userService.checkAdminRole(user)) {
            //是管理员，执行操作
            //保存用户信息时，不保存密码
            user.setPassword(null);
            Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
            String token = JWT.create()
                    .withClaim(Constant.USER_NAME, user.getUsername())
                    .withClaim(Constant.USER_ID, user.getId())
                    .withClaim(Constant.USER_ROLE, user.getRole())
                    //过期时间
                    .withExpiresAt(new Date(System.currentTimeMillis() + Constant.EXPIRE_TIME))
                    .sign(algorithm);
            return ApiRestResponse.success(token);
        } else {
            return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
        }
    }


    /**
     * 更新个性签名
     *
     * @param session
     * @param signature
     * @return
     * @throws MallException
     */
    @PostMapping("/user/update")
    public ApiRestResponse updateUserInfo(HttpSession session, @RequestParam String signature) throws MallException {
        User currentUser = UserFilter.userThreadLocal.get();
        if (currentUser == null) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_LOGIN);
        }
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }

    /**
     * 登出
     *
     * @param session
     * @return
     */
    @PostMapping("/user/logout")
    public ApiRestResponse logout(HttpSession session) {
        session.removeAttribute(Constant.MALL_USER);
        return ApiRestResponse.success();
    }

    /**
     * 管理员登录
     *
     * @param session
     * @param username
     * @param password
     * @return
     * @throws MallException
     */
    @PostMapping("/adminLogin")
    public ApiRestResponse adminLogin(HttpSession session, @RequestParam("userName") String username, @RequestParam("password") String password) throws MallException {
        if (StringUtils.isEmpty(username)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(username, password);
        if (userService.checkAdminRole(user)) {
            user.setPassword(null);
            session.setAttribute(Constant.MALL_USER, user);
            return ApiRestResponse.success(user);
        }
        return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
    }
}
