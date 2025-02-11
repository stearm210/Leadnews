package com.heima.kafka.sample;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.kafka.sample
 * @Author: yanhongwei
 * @CreateTime: 2025-02-11  15:41
 * @Description: TODO
 * @Version: 1.0
 */
 /*
  * @Title:
  * @Author: pyzxW
  * @Date: 2025-02-11 15:43:11
  * @Params:
  * @Return: null
  * @Description: kafka生产者操作
  */
public class ProducerQuickStart {
    public static void main(String[] args) {
        //1.kafka链接配置信息
        Properties properties = new Properties();
        //设置kafka的连接地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.200.130:9092");
        //key和value的序列化类
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

        //2.创建kafka生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        //3.发送消息
        //对应的参数分别为：topic,key,value
        ProducerRecord<String,String> record = new ProducerRecord<String, String>("itheima-topic","100001","hello kafka");
        producer.send(record);

        //4.关闭消息通道。必须关闭，否则消息发送不成功
        producer.close();
    }
}
