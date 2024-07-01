package com.yupi.springbootinit.biRMQ;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.yupi.springbootinit.biRMQ.BiMqConstant.BI_EXCHANGE_NAME;
import static com.yupi.springbootinit.biRMQ.BiMqConstant.BI_ROUTINGKEY_NAME;

@Component
public class BiProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String id){
        //routingKey
        rabbitTemplate.convertAndSend(BI_EXCHANGE_NAME,BI_ROUTINGKEY_NAME,id);
    }

}
