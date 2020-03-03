package ru.otus.demen.books.security;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class GrantedAuthorityImpl implements GrantedAuthority {
    private final String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
