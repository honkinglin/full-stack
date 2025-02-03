package com.fullstack.reader.task;

import com.fullstack.reader.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ComputeTask {
    Logger logger = LoggerFactory.getLogger(ComputeTask.class);

    @Autowired
    private BookService bookService;

    @Scheduled(cron = "0 * * * * ?")
    public void updateScore() {
        bookService.updateScore();
        logger.info("已更新所有图书评分");
    }
}
