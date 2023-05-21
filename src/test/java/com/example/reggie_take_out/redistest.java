package com.example.reggie_take_out;

import com.example.reggie_take_out.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class redistest {
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void test() {
        redisUtil.set("name", "reggie");
        System.out.println(redisUtil.get("name"));
    }
}
