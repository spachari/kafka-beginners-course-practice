package com.github.mylearning.kafka.tutorial1;

//Kafka documentation
//https://kafka.apache.org/documentation/


import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallBack {
    public static void main(String[] args) {

        //This is how we create a simple logger
        final Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallBack.class);

        System.out.println("Hello world");
        String bootstrapServers = "127.0.0.1:9092";
        //create a producer properties

        Properties properties = new Properties();
        //A better way of creating properties is
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create a producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);


        for ( int i = 0; i <= 10; i ++) {

            String topic = "first_topic";
            String value = "Hello World" + Integer.toString(i);

            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<String, String>(topic, value);

            //send data - asynchronous
            producer.send(producerRecord, new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    //executed everytime a record is sent or exception is thrown
                    if (exception == null) {
                        logger.info("Topic: " + metadata.topic() + "\n" +
                                "Partition: " + metadata.partition() + "\n" +
                                "Offset: " + metadata.offset() + "\n" +
                                "Timestamp: " + metadata.timestamp() + "\n");

                    } else {
                        logger.error("Error while producing " + exception);
                    }


                }
            });
            //because it is asynchronous we dont know when it will run
        }

        //flush data
        producer.flush();

        //flush data and close
        producer.close();

    }
}
