package com.office.oa.mapper;

import com.office.oa.entity.Employee;
import com.office.oa.utils.MybatisUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class EmployeeMapperTest {

    @Test
    public void selectById() {
        MybatisUtils.executeQuery(session -> {
            EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);
            System.out.println(employeeMapper.selectById(1L));
            return null;
        });
    }

    @Test
    public void selectByParams() {
        Map params = new HashMap();
        params.put("level", 7);
        params.put("departmentId", 2);
        MybatisUtils.executeQuery(session -> {
            EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);
            List<Employee> employees= employeeMapper.selectByParams(params);
            for (Employee employee : employees) {
                System.out.println(employee);
            }
            return null;
        });
    }

    @Test
    public void selectByParams2() {
        Map params = new HashMap();
        params.put("level", 8);
        MybatisUtils.executeQuery(session -> {
            EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);
            List<Employee> employees= employeeMapper.selectByParams(params);
            for (Employee employee : employees) {
                System.out.println(employee);
            }
            return null;
        });
    }
}