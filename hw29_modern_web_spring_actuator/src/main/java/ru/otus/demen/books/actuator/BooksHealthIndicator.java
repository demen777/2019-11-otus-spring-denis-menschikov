package ru.otus.demen.books.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class BooksHealthIndicator implements HealthIndicator {
    private final RestTemplate restTemplate;
    private final String urlForCheck;

    @Override
    public Health health() {
        int httpStatusCode;
        long timeBefore = System.currentTimeMillis();
        try {
            ResponseEntity<String> responseEntityStr = restTemplate.getForEntity(urlForCheck, String.class);
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
