package com.eventstream.rabbitmq;

import com.eventstream.Consumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RMqConsumer implements Consumer {

    private String name;
    private String exchange;

    private static ConnectionFactory factory = new ConnectionFactory();
    private final static String fanout = "fanout";

    @Override
    public void consume() {

        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(exchange, fanout);
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchange, "");

            System.out.println(
                    String.format(" [*] %s Waiting for messages from %s. To exit press CTRL+C", name, queueName)
            );

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String receivedMessage = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] " + name + " Received '" + receivedMessage + "'");
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });

            Thread.sleep(10000);

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
