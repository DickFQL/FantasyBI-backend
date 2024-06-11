package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class MultiConsumer {

    private static final String TASK_QUEUE_NAME = "multi_queue";

    public static void main(String[] argv) throws Exception {
        //建立连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        //传递回调方法，处理消息
        for (int i = 0; i < 2; i++) {
            final Channel channel = connection.createChannel();
            //声明队列
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            channel.basicQos(1);
            int finalI = i;
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {  //代码 参数问题
                String message = new String(delivery.getBody(), "UTF-8");

                try {
                    System.out.println(" [x] Received 第"+ finalI+"个消息:'" + message + "'");
//                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    Thread.sleep(20000);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    System.out.println(" [x] Done");
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };
            channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> { });
        }

    }


}