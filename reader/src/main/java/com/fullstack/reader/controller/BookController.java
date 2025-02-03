package com.fullstack.reader.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fullstack.reader.entity.Book;
import com.fullstack.reader.service.BookService;
import com.fullstack.reader.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/list")
    public ResponseUtils list(@RequestParam("categoryId") Long categoryId, @RequestParam("order") String order, @RequestParam("page") Integer page) {
        ResponseUtils resp = null;
        try {
            IPage<Book> p = bookService.selectPage(categoryId, order, page, 10);
            resp = new ResponseUtils().put("page", p);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
        return resp;
    }

    @GetMapping("/id/{id}")
    public ResponseUtils selectById(@PathVariable("id") Long id) {
        ResponseUtils resp = null;
        try {
            Book book = bookService.selectById(id);
            resp = new ResponseUtils().put("book", book);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
        return resp;
    }
}

