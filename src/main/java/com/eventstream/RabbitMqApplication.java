package com.eventstream;

import com.eventstream.api.Event;
import com.eventstream.rabbitmq.RMqConsumer;
import com.eventstream.rabbitmq.RMqPublisher;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

public class RabbitMqApplication {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        String exchange = "orders";

        RMqConsumer consumer = new RMqConsumer("sub1", exchange);
        Thread thread = new Thread(consumer::consume);
        thread.start();

        RMqConsumer consumer2 = new RMqConsumer("sub2", exchange);
        Thread thread2 = new Thread(consumer2::consume);
        thread2.start();

        Thread.sleep(2000);

        RMqPublisher publisher = new RMqPublisher(exchange);
        Event event = new Event("1", LocalDateTime.now().toString());
        publisher.publish(event);

        Thread.sleep(10000);

        thread.interrupt();
    }
}
