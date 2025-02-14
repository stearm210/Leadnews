package com.heima.kafka.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.kafka.listener
 * @Author: yanhongwei
 * @CreateTime: 2025-02-14  15:54
 * @Description: TODO
 * @Version: 1.0
 */
//消息消费者
@Component
public class HelloListener {
    @KafkaListener(topics = "itcast-topic")
    public void onMessage(String message){
        if (!StringUtils.isEmpty(message)){
            System.out.println(message);
        }
    }
}
