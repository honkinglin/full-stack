package com.office.oa.mapper;

import com.office.oa.entity.Notice;
import com.office.oa.utils.MybatisUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class NoticeMapperTest {

    @Test
    public void insert() {
        MybatisUtils.executeQuery(session -> {
            NoticeMapper mapper = session.getMapper(NoticeMapper.class);
            Notice notice = new Notice(21l, "Hello, world!");
            mapper.insert(notice);
            return null;
        });
    }
}