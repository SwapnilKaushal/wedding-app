package com.weddingapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:5173",
                                "https://wedding-frontend-six-hazel.vercel.app",
                                "https://wedding-frontend-ju3ag780b-suppykumar-4830s-projects.vercel.app"
                        )
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}
