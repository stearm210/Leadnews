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
    //定义kafka的一个监听效果
    @KafkaListener(topics = ArticleConstants.ARTICLE_ES_SYNC_TOPIC)
     /*
      * @Title: onMessage
      * @Author: pyzxW
      * @Date: 2025-02-16 12:32:14
      * @Params: [msg]
      * @Return: void
      * @Description: 定义监听接收消息,保存索引数据
      * 该函数监听消息队列中的文章数据变更事件，将消息解析后实时同步到Elasticsearch，用于实现搜索服务的数据准实时更新，支撑文章搜索功能。
      */
    public void onMessage(String message) {
        //首先检查传入的消息是否非空，避免处理无效数据。
        if (StringUtils.isNoneBlank(message)){
            //打印对应的信息
            log.info("SyncArticleListener,message={}",message);
            //将JSON格式的字符串消息反序列化为SearchArticleVo对象，提取结构化数据。
            SearchArticleVo searchArticleVo = JSON.parseObject(message, SearchArticleVo.class);
            //创建索引库对象，并且指定索引库的名称
            IndexRequest indexRequest = new IndexRequest("app_info_article");
            //使用文章ID作为Elasticsearch文档的唯一标识。
            indexRequest.id(searchArticleVo.getId().toString());
            //直接将原始JSON消息作为文档内容，保留原始数据格式。
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
