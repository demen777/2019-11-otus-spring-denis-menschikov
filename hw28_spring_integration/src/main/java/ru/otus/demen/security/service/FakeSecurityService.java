package ru.otus.demen.security.service;

import ru.otus.demen.security.model.Security;
import ru.otus.demen.security.model.SecurityClass;

import java.util.Map;

public class FakeSecurityService implements SecurityService {
    private final Map<String, Security> securityMap = Map.of(
            "T", new Security("T", new SecurityClass("SPBXM")),
            "MTSS", new Security("MTSS", new SecurityClass("TQBR")),
            "WRONG_SECURITY_CODE", new Security("WRONG_SECURITY_CODE", new SecurityClass("TQBR"))
            );

    @Override
    public Security get(String code) {
        if (!securityMap.containsKey(code)) {
            throw new IllegalArgumentException(String.format("Not found security for code=%s", code));
        }
        return securityMap.get(code);
    }
}
