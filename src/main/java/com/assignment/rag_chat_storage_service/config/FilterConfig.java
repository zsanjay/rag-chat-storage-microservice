package com.assignment.rag_chat_storage_service.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<ChatAuthFilter> authFilter() {
        FilterRegistrationBean<ChatAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ChatAuthFilter());
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
