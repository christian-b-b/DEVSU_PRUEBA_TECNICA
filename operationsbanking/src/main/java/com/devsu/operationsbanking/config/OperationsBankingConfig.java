package com.devsu.operationsbanking.config;

import com.devsu.operationsbanking.errorhandler.rest.RestErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OperationsBankingConfig {
    @Bean
    public RestTemplate getRestTemplate(){
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return builder.errorHandler(new RestErrorHandler()).build();
    }
}
