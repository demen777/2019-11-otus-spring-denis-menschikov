package ru.otus.demen.books.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/h2-console/**",
                "/favicon.ico",
                "/js/**",
                "/css/**",
                "/error"
        );
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/", "/books", "/book/view")
                .hasAnyRole("USER", "OPERATOR", "ADMIN")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/book/view")
                .hasAnyRole("USER", "OPERATOR", "ADMIN")
                .and()
                .authorizeRequests().antMatchers("/book/edit", "/book/add")
                .hasAnyRole("OPERATOR", "ADMIN")
                .and()
                .authorizeRequests().antMatchers("/**")
                .hasAnyRole("ADMIN")
                .and()
                .anonymous().authorities("ROLE_ANONYMOUS").principal("anonymous")
                .and()
                .formLogin()
                .and()
                .logout();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService)
            throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}
