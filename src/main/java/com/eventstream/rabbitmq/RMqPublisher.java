package com.eventstream.rabbitmq;

import com.eventstream.Publisher;
import com.eventstream.api.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RMqPublisher implements Publisher {

    private String exchange;

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static String fanout = "fanout";
    private static ConnectionFactory factory = new ConnectionFactory();

    @Override
    public Event publish(Event event) throws IOException, TimeoutException {
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchange, fanout);

            String message = objectMapper.writeValueAsString(event);
            channel.basicPublish(exchange, "", null, message.getBytes());

            System.out.println(" [x] Sent '" + message + "'");
        }

        return event;
    }
}
