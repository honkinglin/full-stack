package com.fullstack.reader.controller;

import com.fullstack.reader.service.EvaluationService;
import com.fullstack.reader.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/evaluation")
public class EvaluationController {
    @Autowired
    private EvaluationService evaluationService;

    @GetMapping("/list")
    public ResponseUtils list(@RequestParam("bookId") Long bookId) {
        ResponseUtils resp = null;
        try {
            resp = new ResponseUtils().put("list", evaluationService.selectByBookId(bookId));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }
        return resp;
    }

}
