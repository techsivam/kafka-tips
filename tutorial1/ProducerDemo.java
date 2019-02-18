package com.github.techsivam.kafka.tutorial1;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerDemo {

  public static void main(String[] args) {
    System.out.println("Prodeucer Demo");

    Properties properties = new Properties();
    String bootstramServer = "bootstramServerIP:9092";
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstramServer);
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    KafkaProducer<String,String> producer = new KafkaProducer(properties);
    for (int i=1;i<=10;i++) {
      ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("test",
          "fromIDE Loop : "+i);
      producer.send(producerRecord);


    }
    producer.flush();
    producer.close();
  }
}
