package com.heima.xxljob.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

     /*
      * @Title: shardingJobHandler
      * @Author: pyzxW
      * @Date: 2025-02-22 19:46:38
      * @Params:
      * @Return: null
      * @Description: 分片之业务操作
      */
    @XxlJob("shardingJobHandler")
    public void shardingJobHandler(){
        //分片的参数
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();

        //业务逻辑
        List<Integer> list = getList();
        for (Integer integer : list) {
            if(integer % shardTotal == shardIndex){
                System.out.println("当前第"+shardIndex+"分片执行了，任务项为："+integer);
            }
        }
    }

    public List<Integer> getList(){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
        return list;
    }
}
