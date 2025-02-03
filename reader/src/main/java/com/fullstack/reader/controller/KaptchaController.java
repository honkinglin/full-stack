package com.fullstack.reader.controller;

import com.google.code.kaptcha.Producer;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class KaptchaController {
    @Autowired
    private Producer kapchaProducer;

    @GetMapping("/api/verify_code")
    public void createVerifyCode(HttpServletRequest request, HttpServletResponse response) {
//         响应立即过期
        response.setDateHeader("Expires", 0);
//        不缓存任何内容
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
//        生成验证码
        String text = kapchaProducer.createText();
        request.getSession().setAttribute("kaptchaVerifyCode", text);
        BufferedImage image = kapchaProducer.createImage(text);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
