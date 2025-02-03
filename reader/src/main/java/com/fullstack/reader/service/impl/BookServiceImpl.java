package com.fullstack.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fullstack.reader.entity.Book;
import com.fullstack.reader.mapper.BookMapper;
import com.fullstack.reader.mapper.EvaluationMapper;
import com.fullstack.reader.mapper.MemberReadStateMapper;
import com.fullstack.reader.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private EvaluationMapper evaluationMapper;
    @Autowired
    private MemberReadStateMapper memberReadStateMapper;

    @Override
    public IPage<Book> selectPage(Long categoryId, String order, Integer page, Integer rows) {
        IPage<Book> p = new Page<>(page, rows);
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        if (categoryId != null && categoryId != -1) {
            wrapper.eq("category_id", categoryId);
        }
        if (order != null) {
            if (order.equals("quantity")) {
                wrapper.orderByDesc("evaluation_quantity");
            } else if (order.equals("score")) {
                wrapper.orderByDesc("evaluation_score");
            }
        } else {
            wrapper.orderByDesc("evaluation_quantity");
        }
        p = bookMapper.selectPage(p, wrapper);
        return p;
    }

    @Override
    public Book selectById(Long bookId) {
        return bookMapper.selectById(bookId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateScore() {
        bookMapper.updateScore();
    }

    @Override
    public IPage<Map> selectBookMap(Integer page, Integer rows) {
        IPage p = new Page(page, rows);
        p = bookMapper.selectBookMap(p);
        return p;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Book createBook(Book book) {
        bookMapper.insert(book);
        return book;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Book updateBook(Book book) {
        bookMapper.updateById(book);
        return book;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBook(Long bookId) {
        bookMapper.deleteById(bookId);
        QueryWrapper wrapper1 = new QueryWrapper();
        wrapper1.eq("book_id", bookId);
        evaluationMapper.delete(wrapper1);
        QueryWrapper wrapper2 = new QueryWrapper();
        wrapper2.eq("book_id", bookId);
        memberReadStateMapper.delete(wrapper2);
    }
}
