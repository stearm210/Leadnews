package com.heima.schedule;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@MapperScan("com.heima.schedule.mapper")
@EnableScheduling // 开启定时任务
public class ScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class,args);
    }

 /*
  * @Title:
  * @Author: pyzxW
  * @Date: 2025-01-20 15:05:38
  * @Params:
  * @Return: null
  * @Description: 如果你想实现分页操作，也可以在这里添加对应的mybatis-plus操作进行分页代码编写
  */

    /**
     * mybatis-plus乐观锁支持
     * @return
     */
    @Bean
    public MybatisPlusInterceptor optimisticLockerInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
