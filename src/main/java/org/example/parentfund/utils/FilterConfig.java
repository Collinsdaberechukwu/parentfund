package org.example.parentfund.utils;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyCustomFilter> loggingFilter() {
        FilterRegistrationBean<MyCustomFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MyCustomFilter());
        registrationBean.addUrlPatterns("/*"); // intercept all requests
        return registrationBean;
    }
}
