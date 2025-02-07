package com.fullstack.mall.service;

import com.fullstack.mall.model.pojo.Category;
import com.fullstack.mall.model.request.AddCategoryReq;
import com.fullstack.mall.model.vo.CategoryVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CategoryService {
    void add(AddCategoryReq addCategoryReq);

    void update(Category updateCategoryReq);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    List<CategoryVO> listCategoryForCustomer(Integer parentId);
}
