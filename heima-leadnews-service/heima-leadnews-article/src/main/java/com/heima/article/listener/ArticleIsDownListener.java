package com.heima.article.listener;

import com.alibaba.fastjson.JSON;
import com.heima.article.service.ApArticleConfigService;
import com.heima.common.constants.WmNewsMessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.article.listener
 * @Author: yanhongwei
 * @CreateTime: 2025-02-14  19:26
 * @Description: TODO
 * @Version: 1.0
 */
@Component
@Slf4j
public class ArticleIsDownListener {
    @Autowired
    private ApArticleConfigService apArticleConfigService;
   @KafkaListener(topics = WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN_TOPIC)
    public void onMessage(String message) {
        //对应的消息不为空时
        if (StringUtils.isNotBlank(message)){
            Map map = JSON.parseObject(message, Map.class);
            //修改文章的配置
            apArticleConfigService.updateByMap(map);
            log.info("article端文章配置修改，articleId={}",map.get("articleId"));
        }
    }
}
