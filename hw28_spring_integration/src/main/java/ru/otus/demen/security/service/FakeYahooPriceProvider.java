package ru.otus.demen.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("yahooPriceProvider")
@Slf4j
public class FakeYahooPriceProvider implements YahooPriceProvider {
    @Override
    public BigDecimal getCurrentPrice(String securityCode) {
        if ("WRONG_SECURITY_CODE".equals(securityCode)) {
            throw new IllegalArgumentException(String.format("Not found price for security=%s", securityCode));
        }
        BigDecimal price = BigDecimal.valueOf(securityCode.hashCode());
        log.info("YahooPriceProvider price={} for security={}", price, securityCode);
        return price;
    }
}
