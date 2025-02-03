package com.fullstack.reader.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fullstack.reader.entity.Book;

import java.util.Map;

public interface BookMapper extends BaseMapper<Book> {
    public void updateScore();

    public IPage<Map> selectBookMap(IPage page);
}
