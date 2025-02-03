package com.fullstack.reader.controller;

import com.fullstack.reader.entity.Category;
import com.fullstack.reader.service.CategoryService;
import com.fullstack.reader.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseUtils list() {
        ResponseUtils resp = null;
        try {
            List<Category> categories = categoryService.selectAll();
            resp = new ResponseUtils().put("list", categories);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
        return resp;
    }
}
