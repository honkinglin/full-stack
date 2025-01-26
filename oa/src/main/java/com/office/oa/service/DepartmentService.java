package com.office.oa.service;

import com.office.oa.entity.Department;
import com.office.oa.mapper.DepartmentMapper;
import com.office.oa.utils.MybatisUtils;

public class DepartmentService {
    public Department selectById(Long departmentId) {
        return (Department) MybatisUtils.executeQuery(sqlSession -> {
            DepartmentMapper departmentMapper = sqlSession.getMapper(DepartmentMapper.class);
            return departmentMapper.selectById(departmentId);
        });
    }
}
