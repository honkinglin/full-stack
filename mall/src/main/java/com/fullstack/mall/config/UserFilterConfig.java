package com.fullstack.mall.config;

import com.fullstack.mall.filter.UserFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserFilterConfig {
    @Bean
    public UserFilter userFilter() {
        return new UserFilter();
    }

    @Bean(name = "userFilterConf")
    public FilterRegistrationBean adminFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(userFilter());
        registration.addUrlPatterns("/api/cart/*");
        registration.addUrlPatterns("/api/order/*");
        registration.addUrlPatterns("/api/user/update");
        registration.setName("userFilterConf");
        return registration;
    }
}
