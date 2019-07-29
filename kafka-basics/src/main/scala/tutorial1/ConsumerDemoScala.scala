package tutorial1

import java.time.Duration
import java.util.{Arrays, Properties}

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory

object ConsumerDemoScala extends App {

  val logger = LoggerFactory.getLogger(ConsumerDemoScala.getClass)

  val bootStrapServers = "127.0.0.1:9092"
  val groupId = "my-fourth-application"
  val topic = "first_topic"

  val properties = new Properties
  properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers)
  properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
  properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
  properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "earliest")

  //create consumer
  val consumer : KafkaConsumer[String, String] = new KafkaConsumer[String, String](properties)

  //subscribe consumer to our topic
  consumer.subscribe(Arrays.asList(topic))

  //poll for new data
  while(true) {
    val records : ConsumerRecords[String, String] = consumer.poll(Duration.ofMillis(100))
    import scala.collection.JavaConversions._
    for {record <- records
     consumerRecord : ConsumerRecord[String, String] = record} {
      logger.info("Key : " + consumerRecord.key() + " value, " + consumerRecord.value())
      logger.info("Partition : " + consumerRecord.partition() + " offset : " + consumerRecord.offset())
    }
  }



}
