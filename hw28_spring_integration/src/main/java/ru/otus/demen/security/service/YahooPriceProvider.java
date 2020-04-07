package ru.otus.demen.security.service;

import java.math.BigDecimal;

public interface YahooPriceProvider {
    BigDecimal getCurrentPrice(String securityCode);
}
