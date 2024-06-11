package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class DeadConsumer {

  private static final String EXCHANGE_NAME = "direct_exchange";
    private static final String DEAD_EXCHANGE_NAME = "dead_exchange";
  public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel1 = connection.createChannel();
      channel1.exchangeDeclare(EXCHANGE_NAME, "direct");

      //
      Map<String, Object> hashMap1 = new HashMap<>();
      hashMap1.put("x-dead-letter-exchange",DEAD_EXCHANGE_NAME);
      hashMap1.put("x-dead-letter-routing-key","laoban");
      String queue1 = "xiaowang";
        channel1.queueDeclare(queue1,true,false,false,hashMap1);
        channel1.queueBind(queue1,EXCHANGE_NAME,"wang");

      Map<String, Object> hashMap2 = new HashMap<>();
      hashMap2.put("x-dead-letter-exchange",DEAD_EXCHANGE_NAME);
      hashMap2.put("x-dead-letter-routing-key","waibao");
      String queue2 = "xiaoli";
        channel1.queueDeclare(queue2,true,false,false,hashMap2);
        channel1.queueBind(queue2,EXCHANGE_NAME,"li");


        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), "UTF-8");
            channel1.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            System.out.println(" [xiaowang] Received '" +
            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), "UTF-8");
            channel1.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            System.out.println(" [xiaoli] Received '" +
            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel1.basicConsume(queue1, false, deliverCallback1, consumerTag -> { });
        channel1.basicConsume(queue2, false, deliverCallback2, consumerTag -> { });
  }
}