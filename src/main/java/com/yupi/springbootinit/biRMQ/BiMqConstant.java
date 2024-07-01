package com.yupi.springbootinit.biRMQ;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;


public interface BiMqConstant {

    String BI_EXCHANGE_NAME = "code_exchange";

    String BI_QUEUE_NAME = "code_queue";

    String BI_ROUTINGKEY_NAME = "fantasy";

}
