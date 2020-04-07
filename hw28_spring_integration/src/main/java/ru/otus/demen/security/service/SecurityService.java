package ru.otus.demen.security.service;

import ru.otus.demen.security.model.Security;

public interface SecurityService {
    Security get(String code);
}
