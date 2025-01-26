package com.office.oa.mapper;

import com.office.oa.entity.ProcessFlow;
import com.office.oa.utils.MybatisUtils;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.*;

public class ProcessFlowMapperTest {

    @Test
    public void insert() {
        MybatisUtils.executeQuery(session -> {
            ProcessFlowMapper mapper = session.getMapper(ProcessFlowMapper.class);
            ProcessFlow processFlow = new ProcessFlow();
            processFlow.setFormId(3l);
            processFlow.setOperatorId(2l);
            processFlow.setAction("audit");
            processFlow.setResult("approved");
            processFlow.setReason("同意");
            processFlow.setCreateTime(LocalDateTime.now());
            processFlow.setAuditTime(LocalDateTime.now());
            processFlow.setOrderNo(1);
            processFlow.setState("ready");
            processFlow.setIsLast(1);
            mapper.insert(processFlow);
            return null;
        });
    }
}