package com.office.oa.service;

import com.office.oa.entity.Node;
import com.office.oa.mapper.RbacMapper;

import java.util.List;

public class RbacService {
    private RbacMapper rbacMapper = new RbacMapper();

    public List<Node> selectNodeByUserId(Long userId) {
        return rbacMapper.selectNodeByUserId(userId);
    }
}
