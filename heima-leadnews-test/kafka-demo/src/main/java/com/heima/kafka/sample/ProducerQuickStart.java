package com.heima.kafka.sample;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

/**
 * 生产者
 */
public class ProducerQuickStart {

    public static void main(String[] args) {
        //1.kafka的配置信息
        Properties properties = new Properties();
        //kafka的连接地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.200.130:9092");
        //发送失败，失败的重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG,5);
        //消息key的序列化器
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        //消息value的序列化器
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

        //消息确认机制ack的配置,全部副本确认才返回成功
        properties.put(ProducerConfig.ACKS_CONFIG,"all");

        //消息重置操作
        properties.put(ProducerConfig.RETRIES_CONFIG,10);
        //消息压缩配置
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,"gzip");

        //2.生产者对象
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        //3.发送消息
        //使用kafka_stream流进行测试

        /*
        * 第一个参数是：topic
        * 第二个参数是:消息的key
        * 第三个参数是:消息的value
        * */
        for (int i = 0; i < 10; i++){
            ProducerRecord<String,String> record = new ProducerRecord<String, String>("itheima-topic-input","hello kafka");
            producer.send(record);
        }

        //封装发送的消息
//        ProducerRecord<String,String> record = new ProducerRecord<String, String>("itheima-topic","hello kafka");
        //同步发送消息
//        RecordMetadata recordMetadata = producer.send(record).get();
//        System.out.println(recordMetadata.offset());
        //异步消息发送
//        producer.send(record, new Callback() {
//            @Override
//            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
//                if(e != null){
//                    System.out.println("记录异常信息到日志表中");
//                }
//                //没有问题时直接发送
//                System.out.println(recordMetadata.offset());
//            }
//        });

        //4.关闭消息通道，必须关闭，否则消息发送不成功
        producer.close();
    }
}