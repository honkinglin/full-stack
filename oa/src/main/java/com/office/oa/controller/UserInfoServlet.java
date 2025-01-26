package com.office.oa.controller;

import com.office.oa.entity.Department;
import com.office.oa.entity.Employee;
import com.office.oa.entity.Node;
import com.office.oa.service.DepartmentService;
import com.office.oa.service.EmployeeService;
import com.office.oa.service.RbacService;
import com.office.oa.utils.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/user_info")
public class UserInfoServlet extends HttpServlet {
    private RbacService rbacService = new RbacService();
    private EmployeeService employeeService = new EmployeeService();
    private DepartmentService departmentService = new DepartmentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uid = req.getParameter("uid");
        String eid = req.getParameter("eid");

        if (uid == null || eid == null) {
            resp.setStatus(400);
            return;
        }
        List<Node> nodes = rbacService.selectNodeByUserId(Long.parseLong(uid));
        List<Map> treeList = new ArrayList<>();
        Map module = null;
        for (Node node : nodes) {
            if (node.getNodeType() == 1) {
                module = new LinkedHashMap();
                module.put("node", node);
                module.put("children", new ArrayList<>());
                treeList.add(module);
            } else if (node.getNodeType() == 2) {
                List children = (List) module.get("children");
                children.add(node);
            }
        }
        Employee employee = employeeService.selectById(Long.parseLong(eid));
        Department department = departmentService.selectById(employee.getDepartmentId());

        String json = new ResponseUtils().put("nodeList", treeList).put("employee", employee).put("department", department).toJsonString();

        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().println(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
