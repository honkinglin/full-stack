package com.fullstack.mall.controller;

import com.fullstack.mall.common.ApiRestResponse;
import com.fullstack.mall.model.request.ProductListReq;
import com.fullstack.mall.service.ProductService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * @Description: 商品Controller
 */
@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/product/detail")
    public ApiRestResponse detail(@RequestParam Integer id) {
        return ApiRestResponse.success(productService.detail(id));
    }

    @GetMapping("product/list")
    public ApiRestResponse list(ProductListReq productListReq) {
        PageInfo list = productService.list(productListReq);
        return ApiRestResponse.success(list);
    }
}
