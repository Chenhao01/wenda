package com.nowcoder.configuration;

import com.nowcoder.interceptors.loginInterceptor;
import com.nowcoder.interceptors.testInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by 12274 on 2018/1/17.
 */
@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter{
    @Autowired
    loginInterceptor loginInterceptor;
    @Autowired
    testInterceptor testInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor);
        registry.addInterceptor(testInterceptor).addPathPatterns("/user/*");
        super.addInterceptors(registry);
    }
}
