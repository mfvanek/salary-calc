package com.mfvanek.salary.calc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket publicApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mfvanek.salary.calc.controllers"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData());
    }
//
//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Salary Calculation Demo App")
                .description("\"Spring Boot REST API for Salary Calculation\"")
                .version("1.0.0")
                .license("GNU General Public License v3.0")
                .licenseUrl("https://choosealicense.com/licenses/gpl-3.0/")
                .contact(new Contact("Ivan Vakhrushev", "https://vk.com/mf_vanek", "mfvanek@gmail.com"))
                .build();
    }
}
