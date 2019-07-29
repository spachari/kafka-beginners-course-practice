package com.github.simplesteph.kafka.tutorial4;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import com.google.gson.JsonParser;

public class StreamsFilterTweets {

    private static JsonParser jsonParser = new JsonParser();

    private static Integer extractUserFollowersFromTweet(String tweetJson) {
        //gson library
        try {
            return jsonParser.parse(tweetJson)
                    .getAsJsonObject()
                    .get("user")
                    .getAsJsonObject()
                    .get("followers_count")
                    .getAsInt();
        } catch (Exception e) {
            return 0;
        }

    }

    public static void main(String[] args) {

        //create properties
        Properties properties = new Properties();
        properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "demo-kafka-config");
        properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());

        //create a topology
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        //input topic
        KStream<String, String> inputTopic = streamsBuilder.stream("twitter_tweets");
        KStream<String, String> filteredStream = inputTopic.filter(
                (k,jsonTweet) -> extractUserFollowersFromTweet(jsonTweet) > 10000
                        //filter for stream who have user of 10000 followers
        );
        filteredStream.to("important_tweets");

        //build the topology
        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), properties);


        //start our streams application
        kafkaStreams.start();
    }
}
