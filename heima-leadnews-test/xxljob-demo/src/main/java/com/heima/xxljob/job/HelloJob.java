package com.heima.xxljob.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.xxljob.job
 * @Author: yanhongwei
 * @CreateTime: 2025-02-22  15:34
 * @Description: TODO
 * @Version: 1.0
 */

@Component
public class HelloJob {
     /*
      * @Title: helloJob
      * @Author: pyzxW
      * @Date: 2025-02-22 15:35:13
      * @Params:
      * @Return: null
      * @Description: 简单分布式任务执行
      */
    //demoJobHandler为任务配置中的名字
    @XxlJob("demoJobHandler")
    public void helloJob(){
        System.out.println("简单任务执行了。。。。");
    }
}
