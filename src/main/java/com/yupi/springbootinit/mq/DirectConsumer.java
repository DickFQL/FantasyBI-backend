package com.yupi.springbootinit.mq;

import com.rabbitmq.client.*;

public class DirectConsumer {

  private static final String EXCHANGE_NAME = "direct_exchange";

  public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();

        Channel channel1 = connection.createChannel();
        Channel channel2 = connection.createChannel();

        channel1.exchangeDeclare(EXCHANGE_NAME, "direct");
        channel1.queueDeclare("xiaowang",true,false,false,null);
        channel1.queueBind("xiaowang",EXCHANGE_NAME,"wang");

        channel2.exchangeDeclare(EXCHANGE_NAME,"direct");
        channel2.queueDeclare("xiaoli",true,false,false,null);
        channel2.queueBind("xiaowang",EXCHANGE_NAME,"li");

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
        channel1.basicConsume("xiaowang", true, deliverCallback1, consumerTag -> { });
        channel2.basicConsume("xiaoli", true, deliverCallback2, consumerTag -> { });
  }
}