package twitter.connector

import java.util.Properties
import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue, TimeUnit}

import com.google.common.collect.Lists
import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.core.{Client, Constants, HttpHosts}
import com.twitter.hbc.httpclient.auth.{Authentication, OAuth1}
import connector.{Tweet, TweetKafkaFields}
import org.apache.kafka.clients.producer._
import org.apache.kafka.common.serialization.StringSerializer
import org.json4s.JsonDSL._
import org.json4s.{DefaultFormats, _}
import org.json4s.native.JsonMethods.{render, _}
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}

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

    implicit val formats = DefaultFormats
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
      val tweet = Try(parse(msg).extract[Tweet])

      val extractedTweet = tweet match {
        case Success(value) => {
          println(s"text = ${value.text}, place = ${value.place}, entities = ${value.entities}")

          TweetKafkaFields(value.text, value.place)
        }
        case Failure(value) => TweetKafkaFields("", None)
      }

      val jsonObject = ("text" -> extractedTweet.text) ~ ("place" -> extractedTweet.place.map(_.toString))

      val jsonString = compact(render(jsonObject))
      println(jsonString)
      producer.send(new ProducerRecord[String, String]("twitter_tweets", null, jsonString), new Callback {
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
    val terms = Lists.newArrayList("cricket", "icc", "ben stokes")
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

    //create a safe producer
    properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true")
    properties.setProperty(ProducerConfig.ACKS_CONFIG, "all")
    properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE))
    properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5")
    //kafka 2.0 >= 1.1 so we can keep this as 5. Use 1 otherwise

    //create a high throughput producer at the expense of a bit of latency and CPU usage
    /*
    * A few bits about snappy
    * Snappy is very useful if your messages are text based, for example loglines or JSON documents
    * snappy also has a good balance of CPU / cmpression ratio
    * */

    //We will increase the batch.size = 32KB and introduce a small delay through linger.ms = 20ms
    properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy")
    properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20")
    properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024)) //32 KB batch size


    val producer : KafkaProducer[String, String] = new KafkaProducer[String, String](properties)

    producer

  }


}
