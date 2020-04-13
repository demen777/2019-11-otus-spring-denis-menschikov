package ru.otus.demen.hw33_resilience4j.domain;

import lombok.Value;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Value
public class Product {
    @Id
    String name;
    BigDecimal price;
}
