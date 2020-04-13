package ru.otus.demen.hw33_resilience4j.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.otus.demen.hw33_resilience4j.domain.Product;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainService implements CommandLineRunner {
    private final ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        //noinspection InfiniteLoopStatement
        while (true) {
            Optional<Product> product = productService.get("Кофе");
            log.info(product.toString());
            //noinspection BusyWait
            Thread.sleep(100);
        }
    }
}
