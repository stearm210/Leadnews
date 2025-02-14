package com.heima.kafka.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.kafka.controller
 * @Author: yanhongwei
 * @CreateTime: 2025-02-14  15:50
 * @Description: TODO
 * @Version: 1.0
 */
//消息生产者
@RestController
public class HelloController {
    private KafkaTemplate<String,String> kafkaTemplate;

    @GetMapping("/hello")
    public String hello(){
        kafkaTemplate.send("itcast-topic","黑马程序员");
        return "ok";
    }
}
