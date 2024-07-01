package com.yupi.springbootinit.biRMQ;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MyMessageProducerTest {

    @Resource
    private MyMessageProducer myMessageProducer;
    @Test
    void receiveMessage() {
        myMessageProducer.sendMessage("code_exchange","fantasy","你好呀");
    }
}