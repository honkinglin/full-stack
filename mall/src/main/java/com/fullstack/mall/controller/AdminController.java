package com.fullstack.mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

    @RequestMapping("/admin-page/")
    public String admin() {
        return "forward:/admin/index.html";  // 将请求转发到静态资源
    }
}
