package ru.otus.demen.hw33_resilience4j.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.demen.hw33_resilience4j.domain.Product;
import ru.otus.demen.hw33_resilience4j.repository.ProductRepository;

import javax.tools.Tool;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final LagGenerator lagGenerator;

    private final Map<String, Product> productCache = new HashMap<>();


    @Override
    @CircuitBreaker(name = "productDb", fallbackMethod = "fallback")
    public Optional<Product> get(String name) {
        Optional<Product> product = productRepository.findById(name);
        lagGenerator.run();
        product.ifPresent(product1 -> productCache.put(product1.getName(), product1));
        return product;
    }

    public Optional<Product> fallback(String name, RuntimeException e) {
        if (productCache.containsKey(name)) {
            log.info("Call fallback");
            return Optional.of(productCache.get(name));
        } else {
            return Optional.empty();
        }
    }
}
