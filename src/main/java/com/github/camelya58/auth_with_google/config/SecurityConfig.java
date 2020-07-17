package com.github.camelya58.auth_with_google.config;

import com.github.camelya58.auth_with_google.service.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Class SecurityConfig allows Spring Security help us protect the application and its resources
 * from unauthorized access.
 *
 * @author Kamila Meshcheryakova
 * created 17.07.2020
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Passwords will be encrypted.
     *
     * @return encoded password
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        return passwordEncoder;
    }

    @Autowired
    private AuthProvider authProvider;

    /**
     * Log in using a specially written authentication provider.
     *
     * @param auth of class AuthenticationManagerBuilder
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }

    /**
     * This method allow anonymous users to access the home page, registration and login pages.
     * All other requests must be performed by logged in users. The "/ login" page is set as the login page.
     * <p>
     * In case of successful login, the user will be taken to a page with a list of notes,
     * if an error occurs, he will remain on the login page.
     * <p>
     * In case of successful exit, the user will be taken to the main page.
     *
     * @param http of class HttpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/resources/**", "/", "/login**", "/registration").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login")
                .defaultSuccessUrl("/notes").failureUrl("/login?error").permitAll()
                .and().logout().logoutSuccessUrl("/").permitAll();
    }
}
