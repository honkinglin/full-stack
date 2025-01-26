package com.office.oa.controller;


import com.office.oa.entity.Notice;
import com.office.oa.service.NoticeService;
import com.office.oa.utils.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/notice/list")
public class NoticeServlet extends HttpServlet {
    private NoticeService noticeService = new NoticeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String eid = req.getParameter("eid");
        ResponseUtils res = null;
        resp.setContentType("application/json;charset=utf-8");
        try {
            List<Notice> noticeList = noticeService.getNoticeList(Long.parseLong(eid));
            res = new ResponseUtils().put("list", noticeList);
        } catch (Exception e) {
            e.printStackTrace();
            res = new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
        resp.getWriter().println(res.toJsonString());
    }
}
