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
//这个注解表明该类是一个Spring配置类。
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
     /*
      * @Title: addInterceptors
      * @Author: pyzxW
      * @Date: 2025-02-19 15:33:07
      * @Params:
      * @Return: null
      * @Description: 代码的主要作用是通过拦截器（HandlerInterceptor）来对所有请求（/**）进行拦截处理。
      */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AppTokenInterceptor()).addPathPatterns("/**");
    }
}

