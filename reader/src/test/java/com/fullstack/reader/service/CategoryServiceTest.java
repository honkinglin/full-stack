package com.fullstack.reader.service;

import com.fullstack.reader.entity.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @Test
    public void selectAll() {
        List<Category> categories = categoryService.selectAll();
        for (Category category : categories) {
            System.out.println(category);
        }
    }
}