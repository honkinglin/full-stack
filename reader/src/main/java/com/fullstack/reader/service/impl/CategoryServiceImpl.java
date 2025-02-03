package com.fullstack.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fullstack.reader.entity.Category;
import com.fullstack.reader.mapper.CategoryMapper;
import com.fullstack.reader.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> selectAll() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.orderByAsc("category_id");
        return categoryMapper.selectList(wrapper);
    }
}
