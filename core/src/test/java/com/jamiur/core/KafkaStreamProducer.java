package com.jamiur.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaStreamProducer {

    private final StreamBridge streamBridge;

    @Autowired
    public KafkaStreamProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void sendMessage(String message) {
        streamBridge.send("audit-out-0", message);
        System.out.println("Message sent: " + message);
    }
}