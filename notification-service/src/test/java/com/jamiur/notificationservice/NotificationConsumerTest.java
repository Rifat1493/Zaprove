package com.jamiur.notificationservice;

import com.jamiur.notificationservice.event.ApplicationSubmittedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@SpringBootTest(properties = {
    // Point the app to the embedded kafka broker
    "spring.cloud.stream.kafka.binder.brokers=${spring.embedded.kafka.brokers}"
})
@EmbeddedKafka(
    partitions = 1,
    brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" },
    // The topic our consumer listens to
    topics = { "application.events" }
)
@ExtendWith(OutputCaptureExtension.class)
@DirtiesContext // Ensures the embedded broker is fresh for each test class
class NotificationConsumerTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void shouldConsumeApplicationSubmittedEventAndLogNotifications(CapturedOutput output) {
        // 1. GIVEN a test event
        ApplicationSubmittedEvent testEvent = new ApplicationSubmittedEvent(
                101L,
                42L,
                "PERSONAL_LOAN",
                new BigDecimal("15000.00"),
                LocalDateTime.now()
        );

        // 2. WHEN we send the event to the kafka topic
        kafkaTemplate.send("application.events", testEvent);

        // 3. THEN we assert that the consumer logs the correct notifications
        // We use Awaitility to handle the asynchronous nature of Kafka consumers
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            String logs = output.getOut();
            assertThat(logs).contains("Received ApplicationSubmittedEvent: " + testEvent);
            assertThat(logs).contains(">>> NOTIFYING CUSTOMER [ID: 42]");
            assertThat(logs).contains(">>> NOTIFYING CREDIT OFFICERS: New application #101 is ready for review.");
            assertThat(logs).contains(">>> NOTIFYING RISK OFFICERS: New application #101 is ready for review.");
        });
    }
}
