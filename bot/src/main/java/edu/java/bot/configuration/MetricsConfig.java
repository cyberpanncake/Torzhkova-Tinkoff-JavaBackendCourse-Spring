package edu.java.bot.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter messagesSuccessCounter(MeterRegistry meterRegistry) {
        return Counter.builder("messages.success.count")
            .register(meterRegistry);
    }

    @Bean
    public Counter messagesErrorsCounter(MeterRegistry meterRegistry) {
        return Counter.builder("messages.errors.count")
            .register(meterRegistry);
    }
}
