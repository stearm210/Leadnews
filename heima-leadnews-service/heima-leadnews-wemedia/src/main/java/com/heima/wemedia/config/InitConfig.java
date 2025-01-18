package com.heima.wemedia.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.wemedia.config
 * @Author: yanhongwei
 * @CreateTime: 2025-01-18  16:05
 * @Description: TODO
 * @Version: 1.0
 */

 /*
  * @Title:
  * @Author: pyzxW
  * @Date: 2025-01-18 16:06:57
  * @Params:
  * @Return: null
  * @Description: 在自媒体微服务中添加类，扫描降级代码类的包
  */
@Configuration
@ComponentScan("com.heima.apis.article.fallback")
public class InitConfig {
}
