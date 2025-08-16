package com.jamiur.core;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

    // @Bean
    // public CommandLineRunner sendSampleMessage(KafkaStreamProducer producer) {
    //     return args -> producer.sendMessage("Hello from Spring Cloud Stream!");
    // }
}