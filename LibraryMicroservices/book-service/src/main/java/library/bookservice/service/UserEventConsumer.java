package library.bookservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventConsumer.class);

    @KafkaListener(topics = "user-registered", groupId = "book-service-group")
    public void consumeUserRegistered(String message) {
        LOGGER.info("Received user registration event: {}", message);
    }
}