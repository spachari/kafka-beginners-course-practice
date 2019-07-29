package com.github.mylearning.kafka.tutorial1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class ConsumerDemoWithThreads {
    public static void main(String[] args) {
        new ConsumerDemoWithThreads().run();

    }

    private ConsumerDemoWithThreads() {

    }

    public void run() {
        Logger logger = LoggerFactory.getLogger(ConsumerDemoWithThreads.class);

        String bootstrapServers = "127.0.0.1:9092";
        String group_id = "my-sixth-application";
        String topic = "first_topic";

        //latch for dealing with multiple threads
        CountDownLatch latch = new CountDownLatch(1);

        //Create a consumer runnable
        logger.info("Creating the consumer thread ");
        Runnable myConsumerRunnable = new ConsumerThread(
                bootstrapServers,
                group_id,
                topic,
                latch);

        //start a thread
        Thread myThread = new Thread(myConsumerRunnable);
        myThread.start();

        //add a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread( () ->
        {
            logger.error("Caught shutdown hook");
            ((ConsumerThread) myConsumerRunnable).shutDown();

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.info("Application has exited !!! ");
            }
        }));


        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error("Application got interrupted !!! ", e);
        } finally {
            logger.info("Application is closing !!! ");
        }
    }

    public class ConsumerThread implements Runnable {

        private Logger logger = LoggerFactory.getLogger(ConsumerThread.class.getName());
        private CountDownLatch latch; //this is used to shut down our application correctly
        private KafkaConsumer<String, String> consumer;
        private String topic;

        public ConsumerThread(String bootstrapServers, String group_id, String topic, CountDownLatch latch) {

            logger.info(" topic " + topic + " bootstrapServers " + bootstrapServers + " group_id " + group_id);
            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, group_id);
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            this.consumer = new KafkaConsumer<String, String>(properties);
            consumer.subscribe(Arrays.asList(topic));

            this.latch = latch;

        }

        @Override
        public void run() {
            try {
                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                    for (ConsumerRecord<String, String> record : records) {
                        logger.info("key : " + record.key() + " value : " + record.value());
                        logger.info("Partition : " + record.partition() + " Offset : " + record.partition());
                    }
                }
            } catch (WakeupException e) {
                logger.info("Received shutdown signal!");
            } finally {
                consumer.close();
                latch.countDown(); //Tell our main code (ConsumerDemoWithThreads) we are going to close
            }

        }

        public void shutDown() {
            //the wakeup method is a special method to interrupt consumer.poll()
            //It will make it come up with an exception WakeUpException()
            consumer.wakeup();
        }
    }
}
