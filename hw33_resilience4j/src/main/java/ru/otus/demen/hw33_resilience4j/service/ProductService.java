package ru.otus.demen.hw33_resilience4j.service;

import ru.otus.demen.hw33_resilience4j.domain.Product;

import java.util.Optional;

public interface ProductService {
    Optional<Product> get(String name);
}
