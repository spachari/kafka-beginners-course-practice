package com.github.mylearning.kafka.tutorial1;

//Kafka documentation
//https://kafka.apache.org/documentation/


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerDemo {
    public static void main(String[] args) {
        System.out.println("Hello world");
        String bootstrapServers = "127.0.0.1:9092";
        //create a producer properties

        Properties propertiesOld = new Properties();
        //key.serializer and value.serializer help kafka know what type of value we are sending to kafka and
        //how this will be serialized to bytes.
        //The kafka client will send whatever we send into bytes
        //In out case we seu string for both key and value

        propertiesOld.setProperty("bootstrap.servers",bootstrapServers);
        propertiesOld.setProperty("key.serializer", StringSerializer.class.getName());
        propertiesOld.setProperty("value.serializer",StringSerializer.class.getName());

        Properties properties = new Properties();
        //A better way of creating properties is
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create a producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        ProducerRecord<String, String> producerRecord =
                new ProducerRecord<String, String>("first_topic", "Hello World");

        //send data - asynchronous
        producer.send(producerRecord);
        //because it is asynchronous we dont knwo when it will run

        //flush data
        producer.flush();

        //flush data and close
        producer.close();

    }
}
