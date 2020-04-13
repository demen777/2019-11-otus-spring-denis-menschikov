package ru.otus.demen.hw33_resilience4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.demen.hw33_resilience4j.domain.Product;

public interface ProductRepository extends CrudRepository<Product, String> {
}
