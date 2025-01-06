package com.heima.app.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.app.gateway
 * @Author: yanhongwei
 * @CreateTime: 2025-01-06  15:30
 * @Description: TODO
 * @Version: 1.0
 */

@SpringBootApplication
@EnableDiscoveryClient //开启注册中心
public class AppGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppGatewayApplication.class,args);
    }
}
