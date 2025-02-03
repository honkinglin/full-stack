package com.fullstack.reader.service.impl;

import com.fullstack.reader.entity.Evaluation;
import com.fullstack.reader.mapper.EvaluationMapper;
import com.fullstack.reader.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class EvaluationServiceImpl implements EvaluationService {
    @Autowired
    private EvaluationMapper evaluationMapper;

    @Override
    public List<Map> selectByBookId(Long bookId) {
        return evaluationMapper.selectByBookId(bookId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Evaluation evaluate(Long memberId, Long bookId, Integer score, String content) {
        Evaluation evaluation = new Evaluation();
        evaluation.setMemberId(memberId);
        evaluation.setBookId(bookId);
        evaluation.setScore(score);
        evaluation.setContent(content);
        evaluation.setCreateTime(LocalDateTime.now());
        evaluation.setState("enable");
        evaluation.setEnjoy(0);
        evaluationMapper.insert(evaluation);
        return evaluation;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Evaluation enjoy(Long evaluationId) {
        Evaluation evaluation = evaluationMapper.selectById(evaluationId);
        evaluation.setEnjoy(evaluation.getEnjoy() + 1);
        evaluationMapper.updateById(evaluation);
        return evaluation;
    }
}
