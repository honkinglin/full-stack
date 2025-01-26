package com.office.oa.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.office.oa.entity.User;
import com.office.oa.service.UserService;
import com.office.oa.utils.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        ResponseUtils result;

        try {
            User user = userService.checkLogin(username, password);
            user.setPassword(null);
            user.setSalt(null);
            result = new ResponseUtils().put("user", user);
        } catch (Exception e) {
            result = new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        }
        resp.getWriter().println(result.toJsonString());
    }
}
