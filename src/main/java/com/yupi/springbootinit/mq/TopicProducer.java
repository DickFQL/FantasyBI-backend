package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class TopicProducer {

  private static final String EXCHANGE_NAME = "topic_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String inputArray = scanner.nextLine();
            String[] s = inputArray.split(" ");
            if (s.length<1) continue;
            String message = s[0];
            String routeKey = s[1];
            channel.basicPublish(EXCHANGE_NAME, routeKey, null, message.getBytes("UTF-8"));
            System.out.println(" ["+routeKey+"] Sent '" + message + " to " + routeKey + "'");
        }
    }
  }
}