package com.office.oa.controller;

import com.office.oa.entity.LeaveForm;
import com.office.oa.service.LeaveFormService;
import com.office.oa.utils.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet("/api/leave/*")
public class LeaveFormServlet extends HttpServlet {
    private LeaveFormService leaveFormService = new LeaveFormService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        String uri = req.getRequestURI();
        if (uri.endsWith("/list")) {
            this.list(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        String uri = req.getRequestURI();
        if (uri.endsWith("/create")) {
            this.create(req, resp);
        } else if (uri.endsWith("/audit")) {
            this.audit(req, resp);
        }
    }

    private void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String strEmployeeId = req.getParameter("eid");
        String formType = req.getParameter("formType");
        String startTime = req.getParameter("startTime");
        String endTime = req.getParameter("endTime");
        String reason = req.getParameter("reason");

        LeaveForm form = new LeaveForm();
        form.setEmployeeId(Long.parseLong(strEmployeeId));
        form.setFormType(Integer.parseInt(formType));
        form.setStartTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(startTime)), null));
        form.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(endTime)), null));
        form.setReason(reason);
        form.setCreateTime(LocalDateTime.now());
        ResponseUtils result = null;
        try {
            leaveFormService.createLeaveForm(form);
            result = new ResponseUtils();
        } catch (Exception e) {
            e.printStackTrace();
            result = new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
        resp.getWriter().println(result.toJsonString());
    }

    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String strEmployeeId = req.getParameter("eid");
        ResponseUtils result;
        try {
            List<Map> formList = leaveFormService.getLeaveFormList("processing", Long.parseLong(strEmployeeId));
            result = new ResponseUtils().put("list", formList);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            result = new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
        resp.getWriter().println(result.toJsonString());
    }

    private void audit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String formId = req.getParameter("formId");
        String result = req.getParameter("result");
        String reason = req.getParameter("reason");
        String eid = req.getParameter("eid");
        ResponseUtils res;
        try {
            leaveFormService.audit(Long.parseLong(formId), Long.parseLong(eid), result, reason);
            res = new ResponseUtils();
        } catch (Exception e) {
            e.printStackTrace();
            res = new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
        resp.getWriter().println(res.toJsonString());
    }
}
