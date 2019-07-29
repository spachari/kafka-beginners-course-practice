package kafka.twitter.connector;

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
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TwitterProducerWithHighThroughPutProperties {

    Logger logger = LoggerFactory.getLogger(TwitterProducerWithHighThroughPutProperties.class.getName());
    String consumerKey = "aPrGDjfv78kbpBPOp3RZ3BfMn";
    String consumerSecret = "beqczj0KiXhPkjIdEzaGBpO02t2hRRwXWhuYfzKaxkul5kwXHJ";
    String token = "887022661888561152-uid3YP1nOButwerJpqJu8gAuDMsiFgS";
    String tokenSecret = "HOWe2jWTDZ37gfLct4LVAUZwCJHYhL688lo8QsgPmPp6G";
    List<String> terms = Lists.newArrayList("Ben Stokes", "ICC", "Virat Kohli");

    public TwitterProducerWithHighThroughPutProperties() {

    }

    public static void main(String[] args) {
        new TwitterProducerWithHighThroughPutProperties().run();

    }

    public void run() {
        logger.info("Set Up");
        /** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(10000);

        //Create twitter client
        Client client = createTwitterClient(msgQueue);
        client.connect();
        //create kafka producer
        KafkaProducer<String, String> producer = createKafkaProducer();


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("stopping application ...");
            logger.info("shutting down client from twitter");
            client.stop();
            producer.close();
        }));
        //loop to send tweets to kafka

        while(!client.isDone()) {
            String msg = null;
            try {
                msg = msgQueue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                client.stop();
            }
            if (msg != null) {
                logger.info(msg);
                producer.send(new ProducerRecord<>("twitter_tweets", null, msg), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (exception != null) {
                            logger.error("Something bad happened, please check!!!");
                        }
                    }
                });
            }

        }
        logger.info("End of Application");
    }


    public Client createTwitterClient(BlockingQueue<String> msgQueue) {

        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
// Optional: set up some followings and track terms

        hosebirdEndpoint.trackTerms(terms);

        // These secrets should be read from a config file
        Authentication hosebirdAuth =
                new OAuth1(consumerKey, consumerSecret, token, tokenSecret);

        //Create a client
        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));                          // optional: use this if you want to process client events


        Client hoseBirdClient = builder.build();
        return hoseBirdClient;
    }

    public KafkaProducer<String, String> createKafkaProducer() {
        String bookstrapServer = "127.0.0.1:9092";
        Properties properties = new Properties();
        //A better way of creating properties is
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bookstrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create a safe producer
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");
                                                    //kafka 2.0 >= 1.1 so we can keep this as 5. Use 1 otherwise

        //create a high throughput producer at the expense of a bit of latency and CPU usage
        /*
        * A few bits about snappy
        * Snappy is very useful if your messages are text based, for example loglines or JSON documents
        * snappy also has a good balance of CPU / cmpression ratio
        * */

        //We will increase the batch.size = 32KB and introduce a small delay through linger.ms = 20ms
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024)); //32 KB batch size

        //create a producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        return producer;
    }
}
