package ru.otus.demen.security.model;

import lombok.Value;

@Value
public class Security {
    String code;
    SecurityClass securityClass;
}
