package com.office.oa.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResponseUtilsTest {

    @Test
    public void put() {
        ResponseUtils resp = new ResponseUtils("LoginException", "密码错误").put("key", "value").put("key2", "value2");
        String json = resp.toJsonString();
        System.out.println(json);
    }
}