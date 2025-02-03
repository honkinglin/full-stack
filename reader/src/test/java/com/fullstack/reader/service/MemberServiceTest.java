package com.fullstack.reader.service;

import com.fullstack.reader.entity.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Test
    public void createMember() {
        Member member = memberService.createMember("test", "test", "test");
        assertNotNull(member);
    }

    @Test
    public void checkLogin() {
        Member member = memberService.checkLogin("test", "test");
        assertNotNull(member);
    }
}