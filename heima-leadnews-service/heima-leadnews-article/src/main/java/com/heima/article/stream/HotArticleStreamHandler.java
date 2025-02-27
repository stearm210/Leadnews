package com.heima.article.stream;
 
import com.alibaba.fastjson.JSON;
import com.heima.common.constants.HotArticleConstants;
import com.heima.model.mess.ArticleVisitStreamMess;
import com.heima.model.mess.UpdateArticleMess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;
 
/**
 * 用户行为数据的实时聚合
 */
 
@Configuration
@Slf4j
public class HotArticleStreamHandler {
    @Bean
    public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {
        // 1. 接收消息
        KStream<String, String> stream = streamsBuilder.stream(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC);
 
        // 2. 聚合流式处理
        stream.map((key, value) -> {
            UpdateArticleMess mess = JSON.parseObject(value, UpdateArticleMess.class);
            // 重置消息的key:1234343434 和 value:like:1
            //key为文章的id   value为对应文章的操作
            return new KeyValue<>(mess.getArticleId().toString(), mess.getType().name() + ":" + mess.getAdd());
        })
                // 按照文章id进行聚合
                .groupBy((key, value) -> key)
                // 时间窗口：10秒
                .windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
                // 自行的完成聚合的计算
                .aggregate(new Initializer<String>() {
                    /**
                     * 初始方法，返回值是消息的value
                     *
                     * @return
                     */
                    @Override
                    public String apply() {
                        return "COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0";
                    }
                }, new Aggregator<String, String, String>() {
                    /**
                     * @param key
                     * @param value
                     * @param aggValue
                     * @return
                     */
                    //下面这个apply是真正的聚合操作
                    @Override
                    public String apply(String key, String value, String aggValue) {
                        System.out.println(value);
                        if(StringUtils.isBlank(value)) {
                            return aggValue;
                        }
                        //下面的这个aggAry相当于是对"COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0"做了一个按照逗号的分片操作
                        String[] aggAry = aggValue.split(",");
                        //下面col这些变量是对应的各个文章操作（点赞等）的计数器，初始值都是0
                        int col = 0, com = 0, like = 0, vie = 0;
                        //对aggAry进行遍历
                        for (String agg : aggAry) {
                            //这里又对:进行分片
                            String[] split = agg.split(":");
                            // 获得初始值，也是时间窗口内计算之后的值
                            // 根据新的消息值对相应的计数进行累加，确保计数器保存最新状态
                            //获得赋值为0的状态
                            switch (UpdateArticleMess.UpdateArticleType.valueOf(split[0])) {
                                case COLLECTION:
                                    col = Integer.parseInt(split[1]);
                                    break;
                                case COMMENT:
                                    com = Integer.parseInt(split[1]);
                                    break;
                                case LIKES:
                                    like = Integer.parseInt(split[1]);
                                    break;
                                case VIEWS:
                                    vie = Integer.parseInt(split[1]);
                                    break;
                            }
                        }
                        // 累加操作 例子：likes:1
                        String[] valAry = value.split(":");
                        switch (UpdateArticleMess.UpdateArticleType.valueOf(valAry[0])) {
                            case COLLECTION:
                                col += Integer.parseInt(valAry[1]);
                                break;
                            case COMMENT:
                                com += Integer.parseInt(valAry[1]);
                                break;
                            case LIKES:
                                like += Integer.parseInt(valAry[1]);
                                break;
                            case VIEWS:
                                vie += Integer.parseInt(valAry[1]);
                                break;
                        }
 
                        String formatStr = String.format("COLLECTION:%d,COMMENT:%d,LIKES:%d,VIEWS:%d", col, com, like, vie);
                        System.out.println("文章的id: " + key);
                        System.out.println("当前时间窗口内的消息处理结果: " + formatStr);
                         return formatStr;
                    }
                }, Materialized.as("hot-article-stream-count-001"))
                .toStream()
                .map((key, value) -> {
                    return new KeyValue<>(key.key().toString(), formatObj(key.key().toString(), value));
                })
                //上面的这个formatObj函数用于格式化消息的value数据
                // 发送消息,已经指定了topic
                .to(HotArticleConstants.HOT_ARTICLE_INCR_HANDLE_TOPIC);
 
        return stream;
    }
 
    /**
     * 格式化消息的value数据
     * @param articleId
     * @param value
     * @return
     */
    private String formatObj(String articleId, String value) {
        ArticleVisitStreamMess mess = new ArticleVisitStreamMess();
        mess.setArticleId(Long.valueOf(articleId));
        //COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0
        String[] valAry = value.split(",");
        for (String val : valAry) {
            String[] split = val.split(":");
            switch (UpdateArticleMess.UpdateArticleType.valueOf(split[0])) {
                case COLLECTION:
                    mess.setCollect(Integer.parseInt(split[1]));
                    break;
                case COMMENT:
                    mess.setComment(Integer.parseInt(split[1]));
                    break;
                case LIKES:
                    mess.setLike(Integer.parseInt(split[1]));
                    break;
                case VIEWS:
                    mess.setView(Integer.parseInt(split[1]));
                    break;
            }
        }
 
        log.info("聚合消息处理之后的结果为：{}", JSON.toJSONString(mess));
        return JSON.toJSONString(mess);
    }
}