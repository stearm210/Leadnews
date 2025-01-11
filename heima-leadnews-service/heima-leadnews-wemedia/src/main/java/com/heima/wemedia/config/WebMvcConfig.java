package com.heima.wemedia.config;

import com.heima.wemedia.interceptor.WmTokenInterceptor;
import org.apache.hadoop.classification.InterfaceAudience;
import org.bouncycastle.pqc.crypto.newhope.NHOtherInfoGenerator;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.wemedia.config
 * @Author: yanhongwei
 * @CreateTime: 2025-01-11  19:51
 * @Description: TODO
 * @Version: 1.0
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
     /*
      * @Title: addInterceptors
      * @Author: pyzxW
      * @Date: 2025-01-11 19:54:08
      * @Params:
      * @Return: null
      * @Description: 配置使拦截器生效，拦截所有的请求
      */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //配置使拦截器生效，拦截所有的请求
        registry.addInterceptor(new WmTokenInterceptor()).addPathPatterns("/**");
    }
}
