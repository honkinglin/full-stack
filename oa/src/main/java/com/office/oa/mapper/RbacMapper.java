package com.office.oa.mapper;

import com.office.oa.entity.Node;
import com.office.oa.utils.MybatisUtils;

import java.util.List;

public class RbacMapper {
    public List<Node> selectNodeByUserId(Long userId) {
        List list = (List)MybatisUtils.executeQuery(session -> session.selectList("rbacMapper.selectNodeByUserId", userId));
        return list;
    }
}
