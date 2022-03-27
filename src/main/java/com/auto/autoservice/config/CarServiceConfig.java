package com.auto.autoservice.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class CarServiceConfig {


    // TODO: to make message source work correctly
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
                "classpath:/messages_ua.properties"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
