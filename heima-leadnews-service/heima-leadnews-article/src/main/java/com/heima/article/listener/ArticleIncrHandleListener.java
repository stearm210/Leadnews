package com.heima.article.listener;
 
 
import com.alibaba.fastjson.JSON;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.HotArticleConstants;
import com.heima.model.mess.ArticleVisitStreamMess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
 
@Component
@Slf4j
public class ArticleIncrHandleListener {
    @Autowired
    private ApArticleService apArticleService;

     /*
      * @Title: onMessage
      * @Author: pyzxW
      * @Date: 2025-02-20 14:03:26
      * @Params:
      * @Return: null
      * @Description: 文章分值更新
      */
    @KafkaListener(topics = HotArticleConstants.HOT_ARTICLE_INCR_HANDLE_TOPIC)
    public void onMessage(String msg) {
        if(StringUtils.isNotBlank(msg)) {
            ArticleVisitStreamMess articleVisitStreamMess = JSON.parseObject(msg, ArticleVisitStreamMess.class);
            apArticleService.updateScore(articleVisitStreamMess);
        }
    }
}