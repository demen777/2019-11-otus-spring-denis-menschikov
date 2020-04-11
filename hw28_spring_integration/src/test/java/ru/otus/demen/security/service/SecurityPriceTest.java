package ru.otus.demen.security.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class SecurityPriceTest {
    @Autowired
    SecurityPrice securityPrice;

    @Test
    void getCurrentPrice_yahooPriceProvider() {
        assertThat(securityPrice.getCurrentPrice("T")).isEqualTo(BigDecimal.valueOf("T".hashCode()));
    }

    @Test
    void getCurrentPrice_finamPriceProvider() {
        assertThat(securityPrice.getCurrentPrice("MTSS"))
                .isEqualTo(BigDecimal.valueOf("MTSS".hashCode()/100.0));
    }

    @Test
    void getCurrentPrice_notFoundPrice() {
        assertThatThrownBy(() -> securityPrice.getCurrentPrice("WRONG_SECURITY_CODE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Not found price");
    }

    @Test
    void getCurrentPrice_notFoundSecurity() {
        assertThatThrownBy(() -> securityPrice.getCurrentPrice("ANOTHER_WRONG_SECURITY_CODE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Not found security");
    }
}