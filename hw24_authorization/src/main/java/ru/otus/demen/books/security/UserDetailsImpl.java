package ru.otus.demen.books.security;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDetailsImpl implements UserDetails {
    private final String username;
    private final String hashPassword;
    private final boolean enabled;
    private final List<GrantedAuthority> grantedAuthorities;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableList(grantedAuthorities);
    }

    @Override
    public String getPassword() {
        return hashPassword;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
