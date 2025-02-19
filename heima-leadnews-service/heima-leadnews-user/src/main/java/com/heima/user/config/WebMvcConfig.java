package com.heima.user.config;

import com.heima.user.interceptor.AppTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.user.config
 * @Author: yanhongwei
 * @CreateTime: 2025-02-19  15:29
 * @Description: TODO
 * @Version: 1.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AppTokenInterceptor()).addPathPatterns("/**");
    }
}

