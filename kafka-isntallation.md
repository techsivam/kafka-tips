
sudo yum remove java-1.7.0-openjdk

sudo yum install java-1.8.0

alternatives --config java





ssh -i "demo1.pem" ec2-user@ec2-aws_ec2_public_ip.ap-south-1.compute.amazonaws.com


http://mirror.cc.columbia.edu/pub/software/apache/kafka/2.1.0/kafka_2.12-2.1.0.tgz



tar -xzf kafka_2.12-2.1.0.tgz



rm kafka_2.12-0.10.2.0.tgz



sudo vi .bashrc


source .bashrc

cd kafka_2.12-0.10.2.0
nohup bin/zookeeper-server-start.sh config/zookeeper.properties > ~/zookeeper-logs &



export PATH=/home/ec2-user/kafka_2.12-2.1.0/bin:$PATH




nohup bin/zookeeper-server-start.sh config/zookeeper.properties > ~/zookeeper-logs &


zookeeper-logs




nohup bin/zookeeper-server-start.sh config/zookeeper.properties > ~/zookeeper-logs &


START ZOOKEEPER
nohup bin/zookeeper-server-start.sh config/zookeeper.properties > ~/zookeeper-logs &




START KAFKA
nohup bin/kafka-server-start.sh config/server.properties > ~/kafka-logs &


STOP
bin/kafka-server-stop.sh
bin/zookeeper-server-stop.sh

START
sudo  bin/zookeeper-server-start.sh config/zookeeper.properties
sudo bin/kafka-server-start.sh config/server.properties

add --deamon




CREATE TOPIC

 bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
 bin/kafka-topics.sh --create --zookeeper aws_ec2_public_ip:2181 --replication-factor 2 --partitions 3 --topic mytopic1

 bin/kafka-topics.sh --create --zookeeper aws_ec2_public_ip:2181 --replication-factor 2 --partitions 1 --topic mytopic1


 bin/kafka-topics.sh --create --zookeeper aws_ec2_public_ip:2181 --replication-factor 2 --partitions 1 --topic twitter_tweets

 LIST TOPIC

 bin/kafka-topics.sh --list --zookeeper localhost:2181


 SEND MESSAGE
 > bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test

 bin/kafka-console-producer.sh --broker-list ec2-aws_ec2_public_ip.ap-south-1.compute.amazonaws.com:9092 --topic test


This is a message
This is another message


RECEIVE MESSAHGE


bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning

ec2-aws_ec2_public_ip.ap-south-1.compute.amazonaws.com
bin/kafka-console-consumer.sh --bootstrap-server ec2-aws_ec2_public_ip.ap-south-1.compute.amazonaws.com:9092 --topic test --from-beginning

bin/kafka-console-consumer.sh --bootstrap-server ec2-aws_ec2_public_ip.ap-south-1.compute.amazonaws.com:9092 --topic mytopic1 --from-beginning
bin/kafka-console-consumer.sh --bootstrap-server ec2-aws_ec2_public_ip.ap-south-1.compute.amazonaws.com:9093 --topic mytopic1 --from-beginning

bin/kafka-console-consumer.sh --bootstrap-server ec2-aws_ec2_public_ip.ap-south-1.compute.amazonaws.com:9092 --topic twitter_tweets --from-beginning



DESCRIBE

 bin/kafka-topics.sh  --zookeeper localhost:2181  --topic test --describe



bin/kafka-topics.sh --update --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic test


aws_ec2_public_ip:9092
 bin/kafka-topics.sh --list --zookeeper localhost:2181


--------

ec2-aws_ec2_public_ip.ap-south-1.compute.amazonaws.com



listeners=PLAINTEXT://ec2-aws_ec2_public_ip.ap-south-1.compute.amazonaws.com:9092



listeners=PLAINTEXT://:9092


bin/kafka-consumer-groups.sh --bootstrap-server ec2-aws_ec2_public_ip.ap-south-1.compute.amazonaws.com:9092  --group my-first-application --describe




bin/kafka-console-consumer.sh --bootstrap-server ec2-aws_ec2_public_ip.ap-south-1.compute.amazonaws.com:9092 --topic mytopic1 --from-beginning --group my-first-application
