package ru.otus.demen.security.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.math.BigDecimal;

@MessagingGateway
public interface SecurityPrice {

    @Gateway(requestChannel = "getCurrentPriceInputChannel", replyChannel = "getCurrentPriceOutputChannel")
    BigDecimal getCurrentPrice(String securityCode);
}
