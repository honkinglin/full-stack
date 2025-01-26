package com.office.oa.mapper;

import com.office.oa.entity.LeaveForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LeaveFormMapper {
    public void insert(LeaveForm leaveForm);

    public List<Map> selectByParams(@Param("pf_state") String pfState, @Param("pf_operator_id") Long pfOperatorId);

    public void update(LeaveForm leaveForm);

    public LeaveForm selectById(Long formId);
}
