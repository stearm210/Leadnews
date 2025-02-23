package com.heima.article.job;

import com.heima.article.service.HotArticleService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.article.job
 * @Author: yanhongwei
 * @CreateTime: 2025-02-23  14:19
 * @Description: TODO
 * @Version: 1.0
 */

@Component
@Slf4j
public class ComputeHotArticleJob {
    @Autowired
    private HotArticleService hotArticleService;

     /*
      * @Title: handle
      * @Author: pyzxW
      * @Date: 2025-02-23 14:20:47
      * @Params:
      * @Return: null
      * @Description: 处理操作
      */
    @XxlJob("computeHotArticleJob")
    public void handle(){
        log.info("开始执行计算热点文章任务开始执行");
        hotArticleService.computeHotArticle();
        log.info("开始执行计算热点文章任务结束");
    }
}
