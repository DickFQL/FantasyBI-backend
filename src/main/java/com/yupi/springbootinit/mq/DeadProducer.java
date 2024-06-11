package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Scanner;

public class DeadProducer {

  private static final String EXCHANGE_NAME = "direct_exchange";
  private static final String DEAD_EXCHANGE_NAME = "dead_exchange";
  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel();
         Channel channel1 = connection.createChannel()) {
        channel1.exchangeDeclare(EXCHANGE_NAME, "direct");

        //死信队列1
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME, "direct");
        String queue1 = "laobanq";
        channel.queueDeclare(queue1,true,false,false,null);
        channel.queueBind(queue1,DEAD_EXCHANGE_NAME,"laoban");
        //死信队列2
        String queue2 = "waibaoq";
        channel.queueDeclare(queue2,true,false,false,null);
        channel.queueBind(queue2,DEAD_EXCHANGE_NAME,"waibao");
        //消费者处理1
        DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            System.out.println(" [laoban] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        //消费者处理2
        DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            System.out.println(" [waibao] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queue1, false, deliverCallback1, consumerTag -> { });
        channel.basicConsume(queue2, false, deliverCallback2, consumerTag -> { });

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String inputArray = scanner.nextLine();
            String[] s = inputArray.split(" ");
            if (s.length<1) continue;
            String routeKey = s[1];
            String message = s[0];
            channel1.basicPublish(EXCHANGE_NAME, routeKey, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + " to " + routeKey + "'");
        }
    }
  }
  //..
}