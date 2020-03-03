package ru.otus.demen.books.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.demen.books.dao.UserDao;
import ru.otus.demen.books.model.Role;
import ru.otus.demen.books.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class JpaUserDetailsServiceTest {
    private static final GrantedAuthority ROLE = new GrantedAuthorityImpl("USER");
    private static final User user = new User("ok", "hash", true,
            List.of(new Role(ROLE.getAuthority())));
    private JpaUserDetailsService service;

    UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        service = new JpaUserDetailsService(userDao);
    }

    @Test
    @DisplayName("Успешный поиск пользователя по имени")
    void loadUserByUsername_ok() {
        when(userDao.findById(user.getUsername()))
                .thenReturn(Optional.of(user));
        assertThat(service.loadUserByUsername(user.getUsername()))
                .isEqualTo(new UserDetailsImpl(user.getUsername(), user.getPasswordHash(), user.isEnabled(),
                        List.of(ROLE)));
    }

    @Test
    @DisplayName("Пользователь не найден")
    void loadUserByUsername_fail() {
        final String USERNAME = "fail";
        when(userDao.findById(USERNAME))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.loadUserByUsername(USERNAME)).isInstanceOf(UsernameNotFoundException.class);
    }
}