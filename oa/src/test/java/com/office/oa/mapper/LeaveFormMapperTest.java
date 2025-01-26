package com.office.oa.mapper;

import com.office.oa.entity.LeaveForm;
import com.office.oa.utils.MybatisUtils;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

import static org.junit.Assert.*;

public class LeaveFormMapperTest {

    @Test
    public void insert() {
        MybatisUtils.executeQuery(session -> {
            LeaveFormMapper mapper = session.getMapper(LeaveFormMapper.class);
            LeaveForm form = new LeaveForm();
            form.setEmployeeId(4l); //员工编号
            form.setFormType(1); //事假
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startTime = null;//起始时间
            LocalDateTime endTime = null;//结束时间
            try {
                startTime = LocalDateTime.parse("2020-03-26 08:00:00", formatter);
                endTime = LocalDateTime.parse("2020-04-01 18:00:00", formatter);
            } catch (Exception e) {
                e.printStackTrace();
            }
            form.setStartTime(startTime);
            form.setEndTime(endTime);
            form.setReason("回家探亲");//请假事由
            form.setCreateTime(LocalDateTime.now());//创建时间
            form.setState("processing");//当前状态
            mapper.insert(form);
            return null;
        });
    }

    @Test
    public void selectByParams() {
        MybatisUtils.executeQuery(session -> {
            LeaveFormMapper mapper = session.getMapper(LeaveFormMapper.class);
            List<Map> list = mapper.selectByParams("processing", 2l);
            System.out.println(list);
            return list;
        });
    }
}