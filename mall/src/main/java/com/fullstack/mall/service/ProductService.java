package com.fullstack.mall.service;

import com.fullstack.mall.model.pojo.Product;
import com.fullstack.mall.model.request.AddProductReq;
import com.fullstack.mall.model.request.ProductListReq;
import com.github.pagehelper.PageInfo;

import java.io.File;
import java.io.IOException;

public interface ProductService {
    void add(AddProductReq addProductReq);

    void update(Product updateProduct);

    void delete(Integer id);

    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    Product detail(Integer id);

    PageInfo list(ProductListReq productListReq);

    void addProductByExcel(File file) throws IOException;
}
