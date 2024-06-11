package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

public class DirectProducer {

  private static final String EXCHANGE_NAME = "direct_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String inputArray = scanner.nextLine();
            String[] s = inputArray.split(" ");
            if (s.length<1) continue;
            String routeKey = s[1];
            String message = s[0];
            channel.basicPublish(EXCHANGE_NAME, routeKey, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + " to " + routeKey + "'");
        }



    }
  }
  //..
}