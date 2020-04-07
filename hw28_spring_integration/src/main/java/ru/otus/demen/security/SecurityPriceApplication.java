package ru.otus.demen.security;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.demen.security.service.SecurityPrice;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@SpringBootApplication
public class SecurityPriceApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SecurityPriceApplication.class, args);
        SecurityPrice securityPrice = ctx.getBean(SecurityPrice.class);
        BigDecimal price = securityPrice.getCurrentPrice("T");
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        System.out.println(String.format("price = %s", decimalFormat.format(price)));
    }
}
