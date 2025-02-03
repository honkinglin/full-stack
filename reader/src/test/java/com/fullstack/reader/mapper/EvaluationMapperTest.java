package com.fullstack.reader.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class EvaluationMapperTest {
    @Autowired
    private EvaluationMapper evaluationMapper;

    @Test
    public void selectByBookId() {
        List<Map> evaluations = evaluationMapper.selectByBookId(1L);
        for (Map evaluation : evaluations) {
            System.out.println(evaluation);
        }
    }
}