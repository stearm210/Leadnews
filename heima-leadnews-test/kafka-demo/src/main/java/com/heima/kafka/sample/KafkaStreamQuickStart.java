package com.heima.kafka.sample;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.kafka.sample
 * @Author: yanhongwei
 * @CreateTime: 2025-02-23  19:15
 * @Description: TODO
 * @Version: 1.0
 */
/*
* kafka流式处理
* */
public class KafkaStreamQuickStart {
    public static void main(String[] args) {
        //kafka的配置信心
        //配置对象
        Properties prop = new Properties();
        //连接的配置信息
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.200.130:9092");
        //消息的key序列化器
        prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        /// 消息的value序列化器
        prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        //当前的应用名称
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG,"streams-quickstart");
        //stream的构建起
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        //流式计算
        streamProcessor(streamsBuilder);
        //创建kafka的stearm的对象
        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(),prop);
        //开启流式计算
        kafkaStreams.start();
    }

     /*
      * @Title: streamProcessor
      * @Author: pyzxW
      * @Date: 2025-02-23 19:22:33
      * @Params:消息的内容：hello kafka  hello itcast
      * @Return: null
      * @Description: 流式计算
      */
    private static void streamProcessor(StreamsBuilder streamsBuilder) {
        //创建kstream对象，同时指定从那个topic中接收消息
        KStream<String, String> stream = streamsBuilder.stream("itcast-topic-input");
        //处理消息value
        stream.flatMapValues(new ValueMapper<String, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(String value) {
                        //按照空格进行分隔，将单词分开
                        return Arrays.asList(value.split(" "));
                    }
                })
                //按照value进行聚合处理
                .groupBy((key,value)->value)
                //时间窗口:多少秒进行一次聚合操作？
                .windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
                //统计单词的个数
                .count()
                //转换为kStream
                .toStream()
                .map((key,value)->{
                    System.out.println("key:"+key+",vlaue:"+value);
                    return new KeyValue<>(key.key().toString(),value.toString());
                })
                //发送消息
                .to("itcast-topic-out");
    }
}
