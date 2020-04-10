package ru.otus.demen.books.actuator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActuatorConfiguration {
    @Bean
    public BooksHealthIndicator booksHealthIndicator(
        RestTemplateBuilder restTemplateBuilder,
        @Value("${server.port}") int httpPort,
        @Value("${actuator.health.books.urlForCheck}") String urlForCheck)
    {
        String fullUrl = String.format("http://localhost:%d%s", httpPort, urlForCheck);
        return new BooksHealthIndicator(restTemplateBuilder.build(), fullUrl);
    }
}
