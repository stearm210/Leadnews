package com.heima.wemedia.config;

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
    @Override
    public void addInterceptors(InterceptorRegistry registry){

    }
}
