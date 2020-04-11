package ru.otus.demen.books.actuator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;


@Component
public class BooksHealthIndicator implements HealthIndicator {
    private final RestTemplate restTemplate;
    private final URI uriForCheck;

    public BooksHealthIndicator(RestTemplateBuilder restTemplateBuilder,
                                @Value("${actuator.health.books.host-for-check}") String httpHost,
                                @Value("${server.port}") int httpPort,
                                @Value("${actuator.health.books.path-for-check}") String pathForCheck)
    {
        restTemplate = restTemplateBuilder.build();
        uriForCheck = URI.create(String.format("http://%s:%d%s", httpHost, httpPort, pathForCheck));
    }

    @Override
    public Health health() {
        int httpStatusCode;
        long timeBefore = System.currentTimeMillis();
        try {
            ResponseEntity<String> responseEntityStr = restTemplate.getForEntity(uriForCheck, String.class);
            httpStatusCode = responseEntityStr.getStatusCodeValue();
        } catch (RestClientResponseException error) {
            httpStatusCode = error.getRawStatusCode();
        }
        long requestTime = System.currentTimeMillis() - timeBefore;
        if (httpStatusCode == 200) {
            return Health.up().withDetail("requestTimeMilliseconds", requestTime).build();
        } else {
            return Health.down().withDetail("httpStatusCode", httpStatusCode).build();
        }
    }
}
