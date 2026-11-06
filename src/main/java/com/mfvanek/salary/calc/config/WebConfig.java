package com.mfvanek.salary.calc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Swagger UI is served from the management port, so its "Try it out" requests to the API are cross-origin.
 */
@Configuration(proxyBeanMethods = false)
class WebConfig implements WebMvcConfigurer {

    static final String SWAGGER_UI_ORIGIN = "http://localhost:8090";

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(SWAGGER_UI_ORIGIN)
            .allowedMethods("*");
    }
}
