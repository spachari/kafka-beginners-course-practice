package twitter.connector

import java.util.Properties
import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue, TimeUnit}

import com.google.common.collect.Lists
import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.core.{Client, Constants, HttpHosts}
import com.twitter.hbc.httpclient.auth.{Authentication, OAuth1}
import org.apache.kafka.clients.producer._
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory

object TwitterProducer {

  private[connector] val logger = LoggerFactory.getLogger(TwitterProducer.getClass)
  private[connector] val consumerKey = "aPrGDjfv78kbpBPOp3RZ3BfMn"
  private[connector] val consumerSecret = "beqczj0KiXhPkjIdEzaGBpO02t2hRRwXWhuYfzKaxkul5kwXHJ"
  private[connector] val token = "887022661888561152-uid3YP1nOButwerJpqJu8gAuDMsiFgS"
  private[connector] val tokenSecret = "HOWe2jWTDZ37gfLct4LVAUZwCJHYhL688lo8QsgPmPp6G"

  def main(args: Array[String]): Unit = {
    TwitterProducer.run()
  }



  def run()= {
    //create the client
    val msgQueue : BlockingQueue[String] = new LinkedBlockingQueue[String](10000)

    val client : Client = createTwitterClient(msgQueue)
    client.connect()
    //create the kafka producer
    val producer : KafkaProducer[String, String] = createKafkaProducer()


    val t = new Thread {
      override def run() = {
        logger.info("stopping application ...")
        logger.info("shutting down client from twitter")
        client.stop()
        producer.close()
      }
    }

    sys.addShutdownHook(t.start())

    //loop and send tweets to kafka
    while (!client.isDone) {
      var msg : String = null
      msg = try {
        msgQueue.poll(5, TimeUnit.SECONDS)
      } catch {
        case e : InterruptedException => {
          e.printStackTrace(); client.stop()
          ""
        }
      }
      logger.info(msg)
      producer.send(new ProducerRecord[String, String]("twitter_tweets", null, msg), new Callback {
        override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
          if (exception != null) {
            logger.error("Something bad happened, please check")
          }
        }
      })
    }
    logger.info("End of Application")
  }

  def createTwitterClient(msgQueue: BlockingQueue[String]) = {

    /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
    val hosebirdHosts : HttpHosts = new HttpHosts(Constants.STREAM_HOST)
    val hosebirdEndpoint : StatusesFilterEndpoint = new StatusesFilterEndpoint
    // Optional: set up some followings and track terms
    val terms = Lists.newArrayList("bitcoin")
    hosebirdEndpoint.trackTerms(terms)

    val hosebirdAuth : Authentication = new OAuth1(consumerKey, consumerSecret, token, tokenSecret)

    //Create a client
    val builder = new ClientBuilder()
      .name("Hosebird-Client-01")
      .hosts(hosebirdHosts) // optional: mainly for the logs
      .authentication(hosebirdAuth)
      .endpoint(hosebirdEndpoint)
      .processor(new StringDelimitedProcessor(msgQueue)) // optional: use this if you want to process client events
    val hoseBirdClient: Client = builder.build

    hoseBirdClient

  }

  def createKafkaProducer() = {
    val bootstrapServer = "127.0.0.1:9092"
    val properties = new Properties()
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)

    val producer : KafkaProducer[String, String] = new KafkaProducer[String, String](properties)

    producer

  }


}
