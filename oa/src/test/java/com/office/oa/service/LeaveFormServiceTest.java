package com.office.oa.service;

import com.office.oa.entity.LeaveForm;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.Assert.*;

public class LeaveFormServiceTest {
    LeaveFormService leaveFormService = new LeaveFormService();

    /**
     * 市场部员工请假单(72小时以上)测试用例
     *
     * @throws ParseException
     */
    @Test
    public void createLeaveForm1() throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        LeaveForm form = new LeaveForm();
        form.setEmployeeId(8l);
        form.setStartTime(LocalDateTime.parse("2020032608", formatter));
        form.setEndTime(LocalDateTime.parse("2020040118", formatter));
        form.setFormType(1);
        form.setReason("市场部员工请假单(72小时以上)");
        form.setCreateTime(LocalDateTime.now());
        LeaveForm savedForm = leaveFormService.createLeaveForm(form);
        System.out.println(savedForm.getFormId());
    }

    /**
     * 市场部员工请假单(72小时内)测试用例
     *
     * @throws ParseException
     */
    @Test
    public void createLeaveForm2() throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        LeaveForm form = new LeaveForm();
        form.setEmployeeId(8l);
        form.setStartTime(LocalDateTime.parse("2020032608", formatter));
        form.setEndTime(LocalDateTime.parse("2020032718", formatter));
        form.setFormType(1);
        form.setReason("市场部员工请假单(72小时内)");
        form.setCreateTime(LocalDateTime.now());
        LeaveForm savedForm = leaveFormService.createLeaveForm(form);
        System.out.println(savedForm.getFormId());
    }

    /**
     * 研发部部门经理请假单测试用例
     *
     * @throws ParseException
     */
    @Test
    public void createLeaveForm3() throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        LeaveForm form = new LeaveForm();
        form.setEmployeeId(2l);
        form.setStartTime(LocalDateTime.parse("2020032608", formatter));
        form.setEndTime(LocalDateTime.parse("2020040118", formatter));
        form.setFormType(1);
        form.setReason("研发部部门经理请假单");
        form.setCreateTime(LocalDateTime.now());
        LeaveForm savedForm = leaveFormService.createLeaveForm(form);
        System.out.println(savedForm.getFormId());
    }

    /**
     * 总经理请假单测试用例
     *
     * @throws ParseException
     */
    @Test
    public void createLeaveForm4() throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        LeaveForm form = new LeaveForm();
        form.setEmployeeId(1l);
        form.setStartTime(LocalDateTime.parse("2020032608", formatter));
        form.setEndTime(LocalDateTime.parse("2020040118", formatter));
        form.setFormType(1);
        form.setReason("总经理请假单");
        form.setCreateTime(LocalDateTime.now());
        LeaveForm savedForm = leaveFormService.createLeaveForm(form);
        System.out.println(savedForm.getFormId());
    }

    /**
     * 情况1: 72小时以上请假,部门经理同意,总经理同意,流程结束
     *
     * @throws ParseException
     */
    @Test
    public void audit1() throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        LeaveForm form = new LeaveForm();
        form.setEmployeeId(3l);
        form.setStartTime(LocalDateTime.parse("2020032608", formatter));
        form.setEndTime(LocalDateTime.parse("2020040118", formatter));
        form.setFormType(1);
        form.setReason("研发部员工王美美请假单(72小时以上)");
        form.setCreateTime(LocalDateTime.now());
        LeaveForm savedForm = leaveFormService.createLeaveForm(form);
        System.out.println(savedForm.getFormId());
        leaveFormService.audit(savedForm.getFormId(), 2l, "approved", "部门经理同意");
        leaveFormService.audit(savedForm.getFormId(), 1l, "approved", "总经理同意");
    }

    /**
     * 情况2: 72小时以上请假,部门经理拒绝,流程结束
     *
     * @throws ParseException
     */
    @Test
    public void audit2() throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        LeaveForm form = new LeaveForm();
        form.setEmployeeId(3l);
        form.setStartTime(LocalDateTime.parse("2020032608", formatter));
        form.setEndTime(LocalDateTime.parse("2020040118", formatter));
        form.setFormType(1);
        form.setReason("研发部员工王美美请假单(72小时以上)");
        form.setCreateTime(LocalDateTime.now());
        LeaveForm savedForm = leaveFormService.createLeaveForm(form);
        leaveFormService.audit(savedForm.getFormId(), 2l, "refused", "部门经理拒绝");
    }

    /**
     * 情况3: 72小时以内请假,部门经理同意,流程结束
     *
     * @throws ParseException
     */
    @Test
    public void audit3() throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        LeaveForm form = new LeaveForm();
        form.setEmployeeId(3l);
        form.setStartTime(LocalDateTime.parse("2020032608", formatter));
        form.setEndTime(LocalDateTime.parse("2020032718", formatter));
        form.setFormType(1);
        form.setReason("研发部员工王美美请假单(72小时以内)");
        form.setCreateTime(LocalDateTime.now());
        LeaveForm savedForm = leaveFormService.createLeaveForm(form);
        System.out.println(savedForm.getFormId());
        leaveFormService.audit(savedForm.getFormId(), 2l, "approved", "部门经理同意");
    }
}