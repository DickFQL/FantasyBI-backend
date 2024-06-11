package com.yupi.springbootinit.biRMQ;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MyMessagePro {

    @Resource
    private RabbitTemplate rabbitTemplate;

    void sendMessage(){

    }

}
