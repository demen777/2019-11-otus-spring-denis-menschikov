package ru.otus.demen.hw33_resilience4j.service;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LagGeneratorImpl implements LagGenerator {
    private final LagGeneratorSettings settings;

    private long currentLag;
    private long currentLagIncrement;

    public LagGeneratorImpl(LagGeneratorSettings settings) {
        this.settings = settings;
        currentLagIncrement = settings.getLagIncrement();
        currentLag = 0;
    }

    @SneakyThrows
    @Override
    public void run() {
        if (currentLag + currentLagIncrement > settings.getMaxLagInMilliseconds()
            || currentLag + currentLagIncrement <= 0L)
        {
            currentLagIncrement = -currentLagIncrement;
        }
        currentLag += currentLagIncrement;
        log.info("Lag {} ms", currentLag);
        Thread.sleep(currentLag);
    }
}
