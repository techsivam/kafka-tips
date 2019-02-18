package com.github.techsivam.kafka.tutorial1;

import java.util.Properties;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemoWithCallback {

  public static void main(String[] args) {
    System.out.println("Producer Demo call Back");

    final Logger log = LoggerFactory.getLogger(ProducerDemoWithCallback.class);

    Properties properties = new Properties();
    String bootstramServer = "13.232.174.61:9092";
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstramServer);
    properties
        .setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class.getName());

    KafkaProducer<String, String> producer = new KafkaProducer(properties);
    for (int i = 1; i <= 10; i++) {
      ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("test",
          "fromIDE Callback : " + i);
      producer.send(producerRecord, new Callback() {
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
          if (e == null) {
            log.info("Metadata Received: \n ");
            log.info("\n Topic: " + recordMetadata.topic());
            log.info("\n partition: " + recordMetadata.partition());
            log.info("\n offset: " + recordMetadata.offset());
            log.info("\n timestamp: " + recordMetadata.timestamp());
          } else {
            log.error("Error : ", e);
          }
        }

      });
    }
      producer.flush();
      producer.close();
      System.out.println("Producer Demo call Back ENDS");
    }

}
