package com.github.camelya58.auth_with_google.config;

import com.github.camelya58.auth_with_google.repository.UserRepository;
import com.github.camelya58.auth_with_google.service.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

/**
 * Class SecurityConfig allows Spring Security help us protect the application and its resources
 * from unauthorized access.
 *
 * @author Kamila Meshcheryakova
 * created 17.07.2020
 */
@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    @Autowired
    private AuthProvider authProvider;

    @Autowired
    private UserRepository userRepository;

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

    /**
     * Redirects while enter.
     *
     * @param oAuth2ClientContextFilter is used to create a filter that validates a custom social login request
     * @return the object of class FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean oAuth2ClientFilterRegistration(OAuth2ClientContextFilter oAuth2ClientContextFilter)
    {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(oAuth2ClientContextFilter);
        registration.setOrder(-100);
        return registration;
    }

    private Filter ssoFilter()
    {
        OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/google");
        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oAuth2ClientContext);
        googleFilter.setRestTemplate(googleTemplate);
        CustomUserInfoTokenServices tokenServices = new CustomUserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId());
        tokenServices.setRestTemplate(googleTemplate);
        googleFilter.setTokenServices(tokenServices);
        tokenServices.setUserRepository(userRepository);
        tokenServices.setPasswordEncoder(passwordEncoder);
        return googleFilter;
    }

    /**
     * The filter also needs to know about customer registration through Google.
     * The annotation @ConfigurationProperties indicates which configuration situated in application.properties.
     * @return the object of class AuthorizationCodeResourceDetails
     */
    @Bean
    @ConfigurationProperties("google.client")
    public AuthorizationCodeResourceDetails google()
    {
        return new AuthorizationCodeResourceDetails();
    }

    /**
     * You need to specify the endpoint of the Google user information to complete the authentication.
     * @return the object of class ResourceServerProperties
     */
    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource()
    {
        return new ResourceServerProperties();
    }
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
     * @throws Exception can throw different exceptions
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
        http.addFilterBefore(ssoFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
