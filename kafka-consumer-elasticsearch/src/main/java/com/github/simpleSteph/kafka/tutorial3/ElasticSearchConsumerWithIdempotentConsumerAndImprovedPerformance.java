package com.github.simpleSteph.kafka.tutorial3;

import com.google.gson.JsonParser;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;


public class ElasticSearchConsumerWithIdempotentConsumerAndImprovedPerformance {


    public static RestHighLevelClient createClient() {
        //Copy credentials from elasticsearch account
        //https://6j5qr78rk2:25s07c504q@kafka-course-4599520424.eu-west-1.bonsaisearch.net:443
        String hostName = "kafka-course-4599520424.eu-west-1.bonsaisearch.net";
        String userName = "6j5qr78rk2";
        String password = "25s07c504q";

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));

        RestClientBuilder buider = RestClient.builder(new HttpHost(hostName, 443, "https"))
                .setHttpClientConfigCallback(
                new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });

        RestHighLevelClient client = new RestHighLevelClient(buider);
        return client;

    }

    public static KafkaConsumer<String, String> createConsumer(String topic) {



        String bootstrapServers = "127.0.0.1:9092";
        String group_id = "kafka-demo-elasticSearch";

        //create consumer configs
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, group_id);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        //Enabling autocommit to false
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); //disable autocommit of offsets
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100"); //This will enable only 10 records to be polled max
                                                                                //Just for test (we should receive only 10 records at a time)

        //create consumer
        KafkaConsumer<String, String> consumer =
                new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList(topic));
        return consumer;
    }

    private static JsonParser jsonParser = new JsonParser();

    private static String extractIdFromTweet(String tweetJson) {
        //gson library
        return jsonParser.parse(tweetJson)
                .getAsJsonObject()
                .get("id_str")
                .getAsString();
    }


    public static void main(String[] args) throws IOException {

        Logger logger = LoggerFactory.getLogger(ElasticSearchConsumerWithIdempotentConsumerAndImprovedPerformance.class);
        RestHighLevelClient client = createClient();



        KafkaConsumer<String, String> consumer = createConsumer("twitter_tweets");

        while(true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            Integer recordsReceivedCount = records.count();
            logger.info("Received " + recordsReceivedCount + " records");

            BulkRequest bulkRequest = new BulkRequest();

            for (ConsumerRecord<String, String> record : records) {
                //where we insert data into elastic search

                //There are two strategies for this
                //1. Create a kafka generic id
                //String recordId = record.topic() + "_" + record.partition() + "_" + record.offset();

                //we do this when we cannot find unique value of a record

                //2. Get ID from the tweet itself (id_str)

                try {
                    String recordId = extractIdFromTweet(record.value());

                    IndexRequest indexRequest = new IndexRequest(
                            "twitter",
                            "tweets",
                            recordId
                    ).source(record.value(), XContentType.JSON);

                    bulkRequest.add(indexRequest);
                } catch (NullPointerException e) {
                    logger.warn("skipping bad data : " + record.value());
                }
            }

            if (recordsReceivedCount > 0) { //only commit when we received any records
                BulkResponse bulkItemResponses = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                logger.info("Committing the offsets");
                consumer.commitAsync();
                logger.info("Offsets are committed");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        //client.close();


    }
}
