package com.office.oa.service;

import com.office.oa.entity.Notice;
import com.office.oa.mapper.NoticeMapper;
import com.office.oa.utils.MybatisUtils;

import java.util.List;

public class NoticeService {
    public List<Notice> getNoticeList(Long receiverId) {
        return (List<Notice>) MybatisUtils.executeQuery(sqlSession -> {
            NoticeMapper noticeMapper = sqlSession.getMapper(NoticeMapper.class);
            return noticeMapper.selectByReceiverId(receiverId);
        });
    }
}
