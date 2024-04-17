package edu.java.bot.kafka;

import edu.java.bot.kafka.dlq.DeadLetterQueue;
import edu.java.bot.telegram.LinkUpdateToTgSender;
import edu.java.dto.api.bot.LinkUpdateRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "kafka", name = "enable")
public class UpdatesListener {
    private final LinkUpdateToTgSender sender;
    private final Validator validator;
    private final DeadLetterQueue deadLetterQueue;

    @KafkaListener(topics = "${kafka.topic}", containerFactory = "linkUpdateConsumerKafkaFactory", concurrency = "1")
    public void listenForUpdates(LinkUpdateRequest updateRequest) {
        log.info(updateRequest.toString());
        Optional<String> errors = check(updateRequest);
        if (errors.isPresent()) {
            deadLetterQueue.send(errors.get());
            return;
        }
        try {
            sender.send(updateRequest);
        } catch (Exception e) {
            deadLetterQueue.send(e.getMessage());
        }
    }

    private Optional<String> check(LinkUpdateRequest updateRequest) {
        Set<ConstraintViolation<LinkUpdateRequest>> violations = validator.validate(updateRequest);
        if (violations.isEmpty()) {
            return Optional.empty();
        }
        StringBuilder errors = new StringBuilder("Сообщение имеет неверный формат:\n");
        for (ConstraintViolation<LinkUpdateRequest> violation : violations) {
            errors.append(violation.getMessage());
        }
        return Optional.of(errors.toString());
    }
}
