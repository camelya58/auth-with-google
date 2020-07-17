package com.github.camelya58.auth_with_google.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Class MvcConfig avoids creating of controller for the login page because this page is used by Spring Security
 * and lets Spring Security know which page to use when "/ login".
 *
 * @author Kamila Meshcheryakova
 * created 17.07.2020
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
}
