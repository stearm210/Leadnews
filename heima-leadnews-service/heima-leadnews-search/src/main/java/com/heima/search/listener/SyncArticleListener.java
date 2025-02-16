package com.heima.search.listener;

import com.alibaba.fastjson.JSON;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.search.vos.SearchArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.search.listener
 * @Author: yanhongwei
 * @CreateTime: 2025-02-16  12:31
 * @Description: TODO
 * @Version: 1.0
 */
 /*
  * @Title:
  * @Author: pyzxW
  * @Date: 2025-02-16 12:32:20
  * @Params:
  * @Return: null
  * @Description: 定义监听接收消息,保存索引数据
  */
@Component
@Slf4j
public class SyncArticleListener {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @KafkaListener(topics = ArticleConstants.ARTICLE_ES_SYNC_TOPIC)
     /*
      * @Title: onMessage
      * @Author: pyzxW
      * @Date: 2025-02-16 12:32:14
      * @Params: [msg]
      * @Return: void
      * @Description: 定义监听接收消息,保存索引数据
      */
    public void onMessage(String message) {
        if (StringUtils.isNoneBlank(message)){
            //打印对应的信息
            log.info("SyncArticleListener,message={}",message);
            //转换成对应的对象
            SearchArticleVo searchArticleVo = JSON.parseObject(message, SearchArticleVo.class);
            //创建索引库对象，并且指定索引库的名称
            IndexRequest indexRequest = new IndexRequest("app_info_article");
            //获得文章的id
            indexRequest.id(searchArticleVo.getId().toString());
            indexRequest.source(message, XContentType.JSON);
            try {
                restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("sync es error={}",e);
            }
        }
    }
}
