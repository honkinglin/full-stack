package com.office.oa.service;

import com.office.oa.entity.Employee;
import com.office.oa.entity.LeaveForm;
import com.office.oa.entity.Notice;
import com.office.oa.entity.ProcessFlow;
import com.office.oa.mapper.EmployeeMapper;
import com.office.oa.mapper.LeaveFormMapper;
import com.office.oa.mapper.NoticeMapper;
import com.office.oa.mapper.ProcessFlowMapper;
import com.office.oa.service.exception.LeaveFormException;
import com.office.oa.utils.MybatisUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeaveFormService {
    private EmployeeService employeeService = new EmployeeService();

    public LeaveForm createLeaveForm(LeaveForm form) {
        LeaveForm f = (LeaveForm) MybatisUtils.executeQuery(sqlSession -> {
//            1. 持久化form表单数据,8级以下员工表单状态为processing,8级以上员工表单状态为approved
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = employeeMapper.selectById(form.getEmployeeId());
            if (employee.getLevel() == 8) {
                form.setState("approved");
            } else {
                form.setState("processing");
            }
            LeaveFormMapper leaveFormMapper = sqlSession.getMapper(LeaveFormMapper.class);
            leaveFormMapper.insert(form);
            NoticeMapper noticeMapper = sqlSession.getMapper(NoticeMapper.class);
//            2.增加第一条流程数据,说明表单已提交, 状态为complete
            ProcessFlowMapper processFlowMapper = sqlSession.getMapper(ProcessFlowMapper.class);
            ProcessFlow flow1 = new ProcessFlow();
            flow1.setFormId(form.getFormId());
            flow1.setOperatorId(form.getEmployeeId());
            flow1.setAction("apply");
            flow1.setCreateTime(LocalDateTime.now());
            flow1.setOrderNo(1);
            flow1.setState("complete");
            flow1.setIsLast(0);
            processFlowMapper.insert(flow1);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            3.分情况创建其余流程数据
//            3.1 7级以下员工,生成部门经理审批任务,请假时间大于72小时,还需生成总经理审批任务
            if (employee.getLevel() < 7) {
                Employee dmanager = employeeService.selectLeader(employee.getEmployeeId());
                ProcessFlow flow2 = new ProcessFlow();
                flow2.setFormId(form.getFormId());
                flow2.setOperatorId(dmanager.getEmployeeId());
                flow2.setAction("audit");
                flow2.setCreateTime(LocalDateTime.now());
                flow2.setOrderNo(2);
                flow2.setState("processing");
                long diff = form.getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - form.getStartTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                float hours = diff / (1000 * 60 * 60);
                if (hours >= 72) {
                    flow2.setIsLast(0);
                    processFlowMapper.insert(flow2);
                    Employee manager = employeeService.selectLeader(dmanager.getEmployeeId());
                    ProcessFlow flow3 = new ProcessFlow();
                    flow3.setFormId(form.getFormId());
                    flow3.setOperatorId(manager.getEmployeeId());
                    flow3.setAction("audit");
                    flow3.setCreateTime(LocalDateTime.now());
                    flow3.setOrderNo(3);
                    flow3.setState("ready");
                    flow3.setIsLast(1);
                    processFlowMapper.insert(flow3);
                } else {
                    flow2.setIsLast(1);
                    processFlowMapper.insert(flow2);
                }

                String notice1 = String.format("您的请假时间[%s-%s]已提交,请等待上级审批", form.getStartTime().format(formatter), form.getEndTime().format(formatter));
                noticeMapper.insert(new Notice(employee.getEmployeeId(), notice1));
                String notice2 = String.format("%s-%s提起请假申请[%s-%s],请尽快审批", employee.getTitle(), employee.getName(), form.getStartTime().format(formatter), form.getEndTime().format(formatter));
                noticeMapper.insert(new Notice(dmanager.getEmployeeId(), notice2));

            } else if (employee.getLevel() == 7) {
//            3.2 7级员工, 仅生成总经理审批任务
                Employee manager = employeeService.selectLeader(employee.getEmployeeId());
                ProcessFlow flow2 = new ProcessFlow();
                flow2.setFormId(form.getFormId());
                flow2.setOperatorId(manager.getEmployeeId());
                flow2.setAction("audit");
                flow2.setCreateTime(LocalDateTime.now());
                flow2.setOrderNo(2);
                flow2.setState("ready");
                flow2.setIsLast(1);
                processFlowMapper.insert(flow2);

                String notice1 = String.format("您的请假时间[%s-%s]已提交,请等待上级审批", form.getStartTime().format(formatter), form.getEndTime().format(formatter));
                noticeMapper.insert(new Notice(employee.getEmployeeId(), notice1));
                String notice2 = String.format("%s-%s提起请假申请[%s-%s],请尽快审批", employee.getTitle(), employee.getName(), form.getStartTime().format(formatter), form.getEndTime().format(formatter));
                noticeMapper.insert(new Notice(manager.getEmployeeId(), notice2));

            } else if (employee.getLevel() == 8) {
//            3.3 8级别员工,生成总经理审批任务,系统自动通过
                ProcessFlow flow2 = new ProcessFlow();
                flow2.setFormId(form.getFormId());
                flow2.setOperatorId(employee.getEmployeeId());
                flow2.setAction("audit");
                flow2.setResult("approved");
                flow2.setReason("auto approved");
                flow2.setState("complete");
                flow2.setCreateTime(LocalDateTime.now());
                flow2.setOrderNo(2);
                flow2.setIsLast(1);
                processFlowMapper.insert(flow2);
            }
            return form;
        });
        return f;
    }

    public List<Map> getLeaveFormList(String pfState, Long pfOperatorId) {
        List<Map> list = (List<Map>) MybatisUtils.executeQuery(sqlSession -> {
            LeaveFormMapper mapper = sqlSession.getMapper(LeaveFormMapper.class);
            List<Map> result = mapper.selectByParams(pfState, pfOperatorId);
            return result;
        });
        return list;
    }

    public void audit(Long formId, Long operatorId, String result, String reason) {
        MybatisUtils.executeUpdate(sqlSession -> {
            ProcessFlowMapper processFlowMapper = sqlSession.getMapper(ProcessFlowMapper.class);
            List<ProcessFlow> processFlowList = processFlowMapper.selectByFormId(formId);
            if (processFlowList.size() == 0) {
                throw new LeaveFormException("无效的审批流程");
            }
//            获取当前任务ProcessFlow 对象
            List<ProcessFlow> processList = processFlowList.stream()
                    .filter(flow -> flow.getOperatorId().equals(operatorId) && flow.getState().equals("processing"))
                    .collect(Collectors.toList());
            ProcessFlow process = null;
            if (processList.size() == 0) {
                throw new LeaveFormException("未找到待处理任务节点");
            } else {
                process = processList.getFirst();
                process.setResult(result);
                process.setReason(reason);
                process.setState("complete");
                process.setAuditTime(LocalDateTime.now());
                processFlowMapper.update(process);
            }

            LeaveFormMapper leaveFormMapper = sqlSession.getMapper(LeaveFormMapper.class);
            LeaveForm form = leaveFormMapper.selectById(formId);

            NoticeMapper noticeMapper = sqlSession.getMapper(NoticeMapper.class);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Employee operator = employeeService.selectById(operatorId);
            Employee employee = employeeService.selectById(form.getEmployeeId());
//          如果当前任务是最后一个任务，更新请假单状态 approved/refused
            if (process.getIsLast() == 1) {
                form.setState(result);
                leaveFormMapper.update(form);

                String strResult = result.equals("approved") ? "通过" : "驳回";
                String notice1 = String.format("您的请假申请[%s-%s]%s已%s,审批意见:%s,审批流程已结束", form.getStartTime().format(formatter), form.getEndTime().format(formatter), employee.getName(), strResult, reason);
                noticeMapper.insert(new Notice(employee.getEmployeeId(), notice1));

                String notice2 = String.format("%s-%s的请假申请[%s-%s]已%s,审批意见:%s,审批流程已结束", employee.getTitle(), employee.getName(), form.getStartTime().format(formatter), form.getEndTime().format(formatter), strResult, reason);
                noticeMapper.insert(new Notice(operator.getEmployeeId(), notice2));
            } else {
                List<ProcessFlow> readyList = processFlowList.stream()
                        .filter(flow -> flow.getState().equals("ready")).collect(Collectors.toList());
//                如果当前节点不是最后一个节点且审批通过,那下一个节点的状态从ready变为processing
                if (result.equals("approved")) {
                    ProcessFlow readyFlow = readyList.getFirst();
                    readyFlow.setState("processing");
                    processFlowMapper.update(readyFlow);

//                    消息1:通知提交人，部门经理已经审批通过，交给上级继续审批
                    String notice1 = String.format("您的请假申请[%s-%s]已通过部门经理审批,请等待上级审批", form.getStartTime().format(formatter), form.getEndTime().format(formatter));
                    noticeMapper.insert(new Notice(form.getEmployeeId(), notice1));

//                    消息2：通知总经理有新的审批
                    String notice2 = String.format("%s-%s的请假申请[%s-%s]已通过部门经理审批,请尽快审批", employee.getTitle(), employee.getName(), form.getStartTime().format(formatter), form.getEndTime().format(formatter));
                    noticeMapper.insert(new Notice(readyFlow.getOperatorId(), notice2));

//                    通知3: 通知部门经理（当前经办人）员工的申请单你以批准，交给上级继续审批
                    String notice3 = String.format("%s-%s的请假申请[%s-%s]已通过您的审批,请等待上级审批", employee.getTitle(), employee.getName(), form.getStartTime().format(formatter), form.getEndTime().format(formatter));
                    noticeMapper.insert(new Notice(operator.getEmployeeId(), notice3));
                } else if (result.equals("refused")) {
//                如果当前任务不是最后一个节点且审批驳回,则后续所有任务状态变为cancel, 请假单状态变为refused
                    for (ProcessFlow flow : readyList) {
                        flow.setState("cancel");
                        processFlowMapper.update(flow);
                    }
                    form.setState("refused");
                    leaveFormMapper.update(form);

//                    消息1 通知提交人，审批已驳回，审批流程结束
                    String notice1 = String.format("您的请假申请[%s-%s]已被驳回,审批意见:%s,审批流程已结束", form.getStartTime().format(formatter), form.getEndTime().format(formatter), reason);
                    noticeMapper.insert(new Notice(form.getEmployeeId(), notice1));

//                    消息2: 通知审批人，审批已驳回，审批流程结束
                    String notice2 = String.format("%s-%s的请假申请[%s-%s]已被驳回,审批意见:%s,审批流程已结束", employee.getTitle(), employee.getName(), form.getStartTime().format(formatter), form.getEndTime().format(formatter), reason);
                    noticeMapper.insert(new Notice(operator.getEmployeeId(), notice2));
                }
            }
            return null;
        });
    }
}
