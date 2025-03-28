package com.abasilashvili.user_service.client;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.abasilashvili.user_service.config.context.UserContext;

@Configuration
public class FeignConfig {

    @Bean
    public FeignUserInterceptor feignUserInterceptor(UserContext userContext) {
        return new FeignUserInterceptor(userContext);
    }
}
