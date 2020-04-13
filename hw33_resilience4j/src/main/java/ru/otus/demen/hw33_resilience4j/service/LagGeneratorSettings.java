package ru.otus.demen.hw33_resilience4j.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "lag-generator")
@RequiredArgsConstructor
@ConstructorBinding
@Getter
public class LagGeneratorSettings {
    private final long maxLagInMilliseconds;
    private final long lagIncrement;
}
