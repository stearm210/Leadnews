package com.heima.common.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

 /*
  * @Title:
  * @Author: pyzxW
  * @Date: 2025-02-18 15:31:53
  * @Params:
  * @Return: null
  * @Description: 提供自动化配置默认ObjectMapper，让整个框架自动处理混淆
  */
@Configuration
public class InitJacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper = ConfusionModule.registerModule(objectMapper);
        return objectMapper;
    }

}
