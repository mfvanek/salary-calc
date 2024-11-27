package com.mfvanek.salary.calc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class OpenApiConfig {

    @Value("${info.app.version}")
    private String appVersion;

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
            .info(new Info().title("salary-calc")
                .description("Demo app for salary calculation")
                .version(appVersion)
                .license(new License().name("Apache 2.0").url("https://github.com/mfvanek/salary-calc/blob/master/LICENSE")));
    }
}
