package com.office.oa.mapper;

import com.office.oa.entity.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeMapper {
    public Employee selectById(Long id);
    public List<Employee> selectByParams(Map params);
}
