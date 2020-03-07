package ru.otus.demen.books.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import ru.otus.demen.books.service.*;


@TestConfiguration
@Import(value = {AclConfiguration.class, WebSecurityConfiguration.class, ServiceTestConfiguration.class})
public class AclSecurityTestConfiguration {
}
