# Preparation

##  Docker Compose 

As `root`
`curl -L https://github.com/docker/compose/releases/download/1.7.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose`
`chmod +x /usr/local/bin/docker-compose`

## Docker image (required?)
`docker pull wurstmeister/kafka`

# Run the Zookeeper/Kafka Cluster

* Start a cluster:
    `docker-compose up -d`
* Add more brokers:
    `docker-compose scale kafka=3`
* Destroy a cluster:
    `docker-compose stop`


## Testing 
Access the kafka container
`docker exec -it kafka_kafka_1 bash`

Go to the kafka home dir
`cd /opt/kafka_2.11-0.9.0.1`

Run a console producer
`bin/kafka-console-producer.sh --broker-list localhost:9092 --topic in`

Lookup the Zookeeper host
`docker inspect kafka_zookeeper_1`

Run a console consumer
`bin/kafka-console-consumer.sh --zookeeper <zookeeper-host>:2181 --topic out --from-beginning`


# See also

* http://data-artisans.com/kafka-flink-a-practical-how-to/
* http://kafka.apache.org/081/quickstart.html