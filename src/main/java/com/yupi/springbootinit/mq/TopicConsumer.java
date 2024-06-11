package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class TopicConsumer {

  private static final String EXCHANGE_NAME = "topic_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();


    channel.exchangeDeclare(EXCHANGE_NAME, "topic");
    channel.queueDeclare("xiaowang",true,false,false,null);
    channel.queueBind("xiaowang",EXCHANGE_NAME,"前端.后端");


    channel.exchangeDeclare(EXCHANGE_NAME,"topic");
    channel.queueDeclare("xiaoli",true,false,false,null);
    channel.queueBind("xiaoli",EXCHANGE_NAME,"产品.*");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      System.out.println(" [xiaowang] Received '" +
              delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
    DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      System.out.println(" [xiaoli] Received '" +
              delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
    channel.basicConsume("xiaowang", true, deliverCallback1, consumerTag -> { });
    channel.basicConsume("xiaoli", true, deliverCallback2, consumerTag -> { });
  }
}