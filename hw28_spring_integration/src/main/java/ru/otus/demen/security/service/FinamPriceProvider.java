package ru.otus.demen.security.service;

import java.math.BigDecimal;

public interface FinamPriceProvider {
    BigDecimal getCurrentPrice(String securityCode);
}
