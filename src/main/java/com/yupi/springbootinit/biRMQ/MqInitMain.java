package com.yupi.springbootinit.biRMQ;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

@Component
public class MqInitMain {
    private static final String EXCHANGE_NAME="code_exchange";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            String queueName = "code_queue";
            channel.queueDeclare(queueName,true,false,false,null);
            channel.queueBind(queueName, EXCHANGE_NAME, "fantasy");

            //手动发送消息
//            Scanner scanner = new Scanner(System.in);
//            while (scanner.hasNext()){
//                String inputArray = scanner.nextLine();
//                String[] s = inputArray.split(" ");
//                if (s.length<1) continue;
//                String routeKey = s[1];
//                String message = s[0];
//                channel.basicPublish(EXCHANGE_NAME, routeKey, null, message.getBytes("UTF-8"));
//                System.out.println(" [x] Sent '" + message + " to " + routeKey + "'");
//            }
        }
        catch (Exception e){

        }
    }

}
