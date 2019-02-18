package com.github.techsivam.kafka.tutorial2;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterProducer {


  Logger logger= LoggerFactory.getLogger(TwitterProducer.class.getName());

  public TwitterProducer (){}
  public static void main(String[] args) {
    new TwitterProducer().run();
  }
  public void run(){
    logger.info("Setup");
    // create twitter client
    /** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
    BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(10000);
    Client client = createTwitterClient(msgQueue);
    client.connect();

    // create kafka producer
 KafkaProducer<String,String> kafkaProducer=createKafaProducer();
    // loop and send data to kafka

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      logger.info("Stopping Application");
      logger.info("Stopping Client");

      client.stop();
      logger.info("Closing Producer");
      kafkaProducer.close();
      logger.info("Application End");
    }));
// on a different thread, or multiple different threads....
    while (!client.isDone()) {
      String msg = null;
      try {
        msg = msgQueue.poll(5, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        e.printStackTrace();
        client.stop();
      }
      if(msg !=null){
        logger.info(msg);
        kafkaProducer.send(new ProducerRecord<>("twitter_tweets", null, msg), new Callback() {
          @Override
          public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if(e!=null){
              logger.error("error in sending",e);
            }
          }
        });
      }
      logger.info("End of Applciation");

    }
  }

  private KafkaProducer<String,String> createKafaProducer() {
    Properties properties = new Properties();
    String bootstramServer = "13.232.174.61:9092";
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstramServer);
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    // create safe Producer
    properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
    properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
    properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
    properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5"); // kafka 2.0 >= 1.1 so we can keep this as 5. Use 1 otherwise.
    // high throughput producer (at the expense of a bit of latency and CPU usage)
    properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
    properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
    properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024)); // 32 KB batch size
    KafkaProducer<String,String> producer = new KafkaProducer(properties);
    return producer;
  }

  String consumerKey="consumerKey";
  String consumerSecret="consumerSecret";
  String token="token";
  String secret="secret";

  List<String> terms = Lists.newArrayList("bitcoin", "usa", "politics", "sport", "soccer");

  public Client createTwitterClient(BlockingQueue<String> msgQueue ){


    //  BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

/** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
   // Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
    //String bootstrapServers = "13.232.174.61:9092";

    Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
    StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();


    hosebirdEndpoint.trackTerms(terms);

// These secrets should be read from a config file
    Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token,secret);

    ClientBuilder builder = new ClientBuilder()
        .name("Hosebird-Client-01")                              // optional: mainly for the logs
        .hosts(hosebirdHosts)
        .authentication(hosebirdAuth)
        .endpoint(hosebirdEndpoint)
        .processor(new StringDelimitedProcessor(msgQueue));
        //.eventMessageQueue(eventQueue);                          // optional: use this if you want to process client events

    Client hosebirdClient = builder.build();
// Attempts to establish a connection.
    return  hosebirdClient;

  }
}
