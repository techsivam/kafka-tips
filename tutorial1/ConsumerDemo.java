package com.github.techsivam.kafka.tutorial1;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerDemo {

  public static void main(String[] args) {


    final Logger log = LoggerFactory.getLogger(ConsumerDemo.class);

    String bootstramServer = "bootstramServerIP:9092";
    String topic="mytopic1";
    String groupid="my-second-application";



    Properties properties=new Properties();
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstramServer);
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    KafkaConsumer<String,String> consumer=new KafkaConsumer<String, String>(properties);
    consumer.subscribe(Arrays.asList(topic));
    while (true){

      ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));

      for (ConsumerRecord<String,String> consumerRecord:consumerRecords
      ) {
        log.info("Key: "+consumerRecord.key() +" Value:"+consumerRecord.value());
        log.info("Partition: "+consumerRecord.partition() +" Offset:"+consumerRecord.offset());

      }

    }

  }
}
