package ru.otus.demen.books.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.demen.books.dao.UserDao;
import ru.otus.demen.books.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private static final GrantedAuthority ROLE = new GrantedAuthorityImpl("USER");
    @NonNull
    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findById(username).orElseThrow(() ->
        {
            throw new UsernameNotFoundException(String.format("Username=%s not found in DB", username));
        });
        return userDetailsFromUser(user);
    }

    private UserDetails userDetailsFromUser(User user) {
        List<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
                .map(role -> new GrantedAuthorityImpl(role.getName()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(user.getUsername(), user.getPasswordHash(), user.isEnabled(), grantedAuthorities);
    }
}
