package kafka.tutorial1;

//Kafka documentation
//https://kafka.apache.org/documentation/


import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerDemoWithCallBackWithKeys {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //This is how we create a simple logger
        final Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallBackWithKeys.class);

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


        for ( int i = 0; i <= 100; i ++) {

            String topic = "third_topic";
            String value = "Hello World" + Integer.toString(i);
            String key = "id_" + Integer.toString(i);

            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<String, String>(topic, key, value);

            logger.info("key: " + key);

            //Note the following
            // id_0 is going through parition 1
            // id_1 is going through parition 0
            // id_2 is going through parition 2
            // id_3 is going through parition 0
            // id_4 is going through parition 2
            // id_5 is going through parition 2
            // id_6 is going through parition 0
            // id_7 is going through parition 2
            // id_8 is going through parition 1
            // id_9 is going through parition 2
            // id_10 is going through parition 2

            //No matter how many times we run this code, the keys with the same id will go through the same partition.
            //

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
            }).get(); //block the send to make it synchronous - don't do this in production
        }

        //Points to Note:
        //1. If you see all keys go through the same partition, then it means that out topic has only one partition
        //2. If you have anything other than 3 partitions, then the results will vary in terms of which key will go to which partition.
        //3. If we have 3 partitions and the keys are  in this form, "id_" + Integer.toString(i), the data will
        //always go through this form

        //flush data
        producer.flush();

        //flush data and close
        producer.close();

    }
}
